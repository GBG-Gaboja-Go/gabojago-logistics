package com.gbg.deliveryservice.application.service.impl;

import com.gabojago.exception.AppException;
import com.gbg.deliveryservice.application.helper.AIMessageHelper;
import com.gbg.deliveryservice.application.service.AIService;
import com.gbg.deliveryservice.domain.repository.AIRepository;
import com.gbg.deliveryservice.infrastructure.client.dto.GeminiRequest;
import com.gbg.deliveryservice.presentation.advice.AIErrorCode;
import com.gbg.deliveryservice.presentation.dto.request.InternalCreateAIRequestDto;
import com.gbg.deliveryservice.presentation.dto.request.InternalCreateAIRequestDto.AIDto.Location;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIServiceImpl implements AIService {

    @Value("${ai.gemini.api-key}")
    private String apiKey;

    @Value("${ai.gemini.base-url}")
    private String baseUrl;
    @Value("${ai.gemini.default-model}")
    private String defaultModel;

    private final WebClient webClient = WebClient.create();
    private final AIMessageHelper aiMessageHelper;
    private final AIRepository aiRepository;

    @Override
    public void createShippingDeadline(
        InternalCreateAIRequestDto requestDto) {
        // messages 생성
        String prompt = aiMessageHelper.buildPromptForDeadline(requestDto);
        log.info("message: {}", prompt);
        log.info("API 키 앞 4자리: {}", apiKey.substring(0, 4));

        try {
            // AI 호출
            // 타임아웃 설정 (30초)
            String aiRaw = chat(prompt, defaultModel)
                .timeout(Duration.ofSeconds(30))
                .block();

            log.info("AI 응답: {}", aiRaw);

            // ai 응답에서 최종발송시한값 parse
            LocalDateTime finalDeadline = aiMessageHelper.parseFinalDeadlineOnly(
                aiRaw,
                requestDto.getAi().getOrderTime()
            );

            // slack에 보낼 메시지 format
            String responseMessage = formattingResponse(requestDto, finalDeadline);
            log.info("최종 응답: {}", responseMessage);

        } catch (Exception e) {
            log.error("AI 처리 중 오류 발생", e);
            throw new RuntimeException("배송 시한 계산 실패: " + e.getMessage(), e);
        }

        // slack 찌르기

    }

    private Mono<String> chat(String prompt, String model) {
        GeminiRequest body = GeminiRequest.fromPrompt(prompt);

        log.info("=== Gemini API 호출 ===");
        log.info("URL: {}/chat/completions", baseUrl);
        log.info("Model: {}", model);

        return webClient.post()
            .uri(baseUrl + "/models/" + model + ":generateContent" + "?key=" + apiKey)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .onStatus(
                status -> status.value() == 401 || status.value() == 403,
                response -> response.bodyToMono(String.class)
                    .flatMap(errorBody -> {
                        log.error("Gemini 인증 실패: {}", errorBody);
                        return Mono.error(new AppException(AIErrorCode.GEMINI_UNAUTHORIZED));
                    })
            )
            .bodyToMono(Map.class)
            .map(resp -> {
                try {
                    // Gemini API 응답 구조: candidates[0].content.parts[0].text
                    List<?> candidates = (List<?>) resp.get("candidates");
                    if (candidates != null && !candidates.isEmpty()) {
                        Map<?, ?> firstCandidate = (Map<?, ?>) candidates.get(0);
                        Map<?, ?> content = (Map<?, ?>) firstCandidate.get("content");
                        if (content != null) {
                            List<?> parts = (List<?>) content.get("parts");
                            if (parts != null && !parts.isEmpty()) {
                                Map<?, ?> part = (Map<?, ?>) parts.get(0);
                                return (String) part.get("text"); // 최종 응답 텍스트
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("Gemini 응답 파싱 실패", e);
                }
                log.warn("예상하지 못한 응답: {}", resp);
                return resp.toString();
            });
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
