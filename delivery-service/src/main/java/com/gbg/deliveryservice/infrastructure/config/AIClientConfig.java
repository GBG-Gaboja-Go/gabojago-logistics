package com.gbg.deliveryservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AIClientConfig {

    @Bean
    public WebClient geminiWebClient(@Value("${ai.gemini.base-url}") String baseUrl) {
        // base URI 및 기타 설정(타임아웃, 필터 등)을 여기서 일괄 관리합니다.
        return WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }
}
