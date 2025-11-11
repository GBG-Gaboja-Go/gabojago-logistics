// src/main/java/com/gbg/hubservice/infrastructure/config/kakao/KakaoNaviConfig.java
package com.gbg.hubservice.infrastructure.config.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class KakaoNaviConfig {

    private final KakaoApiProperties api;
    private final KakaoNaviProperties props;

    @Bean
    public WebClient kakaoNaviWebClient(WebClient.Builder builder) {
        return builder
            .baseUrl(props.baseUrl())
            .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + api.key())
            .build();
    }
}
