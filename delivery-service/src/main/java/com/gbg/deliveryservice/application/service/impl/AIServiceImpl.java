package com.gbg.deliveryservice.application.service.impl;

import com.gbg.deliveryservice.application.helper.AIMessageHelper;
import com.gbg.deliveryservice.application.service.AIService;
import com.gbg.deliveryservice.domain.entity.AIHistory;
import com.gbg.deliveryservice.domain.repository.AIRepository;
import com.gbg.deliveryservice.infrastructure.client.GeminiClient;
import com.gbg.deliveryservice.presentation.dto.request.InternalCreateAIRequestDto;
import com.gbg.deliveryservice.presentation.dto.request.InternalCreateAIRequestDto.AIDto;
import com.gbg.deliveryservice.presentation.dto.request.InternalCreateAIRequestDto.AIDto.Location;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIServiceImpl implements AIService {

    private final GeminiClient geminiClient;
    private final AIMessageHelper aiMessageHelper;
    private final AIRepository aiRepository;

    @Override
    public void createShippingDeadline(
        InternalCreateAIRequestDto requestDto) {
        AIDto aiDto = requestDto.getAi();
        // prompt 생성
        String prompt = aiMessageHelper.buildPromptForDeadline(requestDto);
        log.info("message: {}", prompt);

        try {
            String aiRaw = geminiClient.generate(prompt)
                .timeout(Duration.ofSeconds(30))
                .block();

            log.info("AI 응답: {}", aiRaw);

            // ai 응답에서 최종발송시한값 parse
            LocalDateTime finalDeadline = aiMessageHelper.parseFinalDeadlineOnly(
                aiRaw,
                requestDto.getAi().getOrderTime()
            );

            // slack에 보낼 메시지 format
            String responseFromAI = formattingResponse(requestDto, finalDeadline);
            log.info("responseFromAI: {}", responseFromAI);

            // repository 저장
            AIHistory history = AIHistory.builder()
                .orderId(aiDto.getOrderId())
                .orderRequestMessage(aiDto.getOrderRequestMessage())
                .deliveryManSlackEmail(aiDto.getDeliveryManSlackEmail()) // 생략 가능
                .finalDeadline(finalDeadline)
                .responseMessage(responseFromAI)
                .build();

            aiRepository.save(history);
            // slack 찌르기

        } catch (Exception e) {
            log.error("AI 처리 중 오류 발생", e);
            throw new RuntimeException("배송 시한 계산 실패: " + e.getMessage(), e);
        }

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
