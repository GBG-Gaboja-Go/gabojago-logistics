package com.gbg.deliveryservice.infrastructure.client;

import com.gabojago.exception.AppException;
import com.gbg.deliveryservice.infrastructure.client.dto.GeminiRequest;
import com.gbg.deliveryservice.infrastructure.client.dto.GeminiResponse;
import com.gbg.deliveryservice.presentation.advice.AIErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeminiClient {

    private final WebClient geminiWebClient;

    @Value("${ai.gemini.api-key}")
    private String apiKey;
    @Value("${ai.gemini.base-url}")
    private String baseUrl;
    @Value("${ai.gemini.default-model}")
    private String defaultModel;

    public Mono<String> generate(String prompt) {
        GeminiRequest body = GeminiRequest.fromPrompt(prompt);

        log.info("=== Gemini API 호출 ===");
        log.info("URL: {}/chat/completions", baseUrl);
        log.info("Model: {}", defaultModel);

        return geminiWebClient.post()
            .uri(baseUrl + "/models/" + defaultModel + ":generateContent" + "?key=" + apiKey)
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
            .bodyToMono(GeminiResponse.class)
            .map(resp -> {
                String resultText = resp.extractText();
                if (resultText == null || resultText.isEmpty()) {
                    log.warn("Gemini 응답에 텍스트가 없습니다: {}", resp);
                    return ""; // 또는 적절한 예외 처리
                }
                return resultText;
            })
            .onErrorMap(e -> {
                return new AppException(AIErrorCode.AI_RESPONSE_PARSING_FAILED);
            });
    }
}
