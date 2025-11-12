package com.gbg.deliveryservice.application.service.impl;

import com.gabojago.util.PageableUtils;
import com.gbg.deliveryservice.application.helper.AIPromptHelper;
import com.gbg.deliveryservice.application.service.AIService;
import com.gbg.deliveryservice.domain.entity.AIHistory;
import com.gbg.deliveryservice.domain.repository.AIRepository;
import com.gbg.deliveryservice.infrastructure.client.GeminiClient;
import com.gbg.deliveryservice.infrastructure.client.SlackFeignClient;
import com.gbg.deliveryservice.infrastructure.client.dto.SlackSendDmRequest;
import com.gbg.deliveryservice.presentation.dto.request.InternalCreateAIRequestDto;
import com.gbg.deliveryservice.presentation.dto.request.InternalCreateAIRequestDto.AIDto;
import com.gbg.deliveryservice.presentation.dto.request.InternalCreateAIRequestDto.AIDto.Location;
import com.gbg.deliveryservice.presentation.dto.response.GetAIResponseDto;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIServiceImpl implements AIService {

    private final GeminiClient geminiClient;
    private final AIPromptHelper aiPromptHelper;
    private final AIRepository aiRepository;
    private final SlackFeignClient slackFeignClient;

    @Override
    public void createShippingDeadline(
        InternalCreateAIRequestDto requestDto) {
        AIDto aiDto = requestDto.getAi();
        // prompt 생성
        String prompt = aiPromptHelper.buildPromptForDeadline(requestDto);
        log.info("message: {}", prompt);

        try {
            String aiRaw = geminiClient.generate(prompt)
                .timeout(Duration.ofSeconds(90))
                .block();

            log.info("AI 응답: {}", aiRaw);

            // ai 응답에서 최종발송시한값 parse
            LocalDateTime finalDeadline = aiPromptHelper.parseFinalDeadlineOnly(
                aiRaw,
                aiDto.getOrderTime()
            );

            // slack에 보낼 메시지 format
            String responseMessage = formattingResponse(requestDto, finalDeadline);
            log.info("responseFromAI: {}", responseMessage);

            // repository 저장
            AIHistory history = AIHistory.builder()
                .orderId(aiDto.getOrderId())
                .orderRequestMessage(aiDto.getOrderRequestMessage())
                .deliveryManSlackEmail(aiDto.getDeliveryManSlackEmail())
                .finalDeadline(finalDeadline)
                .responseMessage(responseMessage)
                .build();

            aiRepository.save(history);

            // slack 찌르기
            SlackSendDmRequest slackRequestDto = SlackSendDmRequest.builder()
                .email(aiDto.getDeliveryManSlackEmail())
                .message(responseMessage)
                .build();
            slackFeignClient.sendDm(slackRequestDto);

        } catch (Exception e) {
            log.error("AI 처리 중 오류 발생", e);
            throw new RuntimeException("배송 시한 계산 실패: " + e.getMessage(), e);
        }

    }

    @Override
    public Page<GetAIResponseDto> getAllLogs(Pageable pageable) {
        pageable = PageableUtils.normalize(pageable);
        Page<AIHistory> historyPage = aiRepository.findAll(pageable);

        return historyPage.map(history ->
            GetAIResponseDto.builder()
                .ai(GetAIResponseDto.AIDto.builder()
                    .orderId(history.getOrderId())
                    .orderRequestMessage(history.getOrderRequestMessage())
                    .aiResponseMessage(history.getResponseMessage()) // 엔티티의 responseMessage 필드 가정
                    .finalDeadline(history.getFinalDeadline())
                    .build())
                .build()
        );
    }

    private String formattingResponse(InternalCreateAIRequestDto requestDto,
        LocalDateTime finalDeadline) {
        return String.format("""
                주문 번호 : %s
                주문자 정보 : %s / %s
                주문 시간 : %s
                상품 정보 : %s %d박스
                요청 사항 : %s
                발송지 : %s
                경유지 : %s
                도착지 : %s
                배송담당자 : %s / %s

                위 내용을 기반으로 도출된 최종 발송 시한은 %s 입니다.
                """,
            requestDto.getAi().getOrderId(),
            requestDto.getAi().getOrdererName(),
            requestDto.getAi().getOrdererEmail(),
            requestDto.getAi().getOrderTime(),
            requestDto.getAi().getProductName(),
            requestDto.getAi().getProductQuantity(),
            requestDto.getAi().getOrderRequestMessage(),
            requestDto.getAi().getPickupHub().getName(),
            requestDto.getAi().getViaHub() != null
                ? requestDto.getAi().getViaHub().stream()
                .map(Location::getName).toList()
                : "없음",
            requestDto.getAi().getDestinationAddress(),
            requestDto.getAi().getDeliveryPersonName(),
            requestDto.getAi().getDeliveryManSlackEmail(),
            finalDeadline
        );

    }
}
