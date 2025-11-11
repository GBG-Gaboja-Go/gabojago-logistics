package com.gbg.hubservice.infrastructure.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 카카오 로컬(지오코딩) 전용 WebClient - baseUrl: https://dapi.kakao.com - 인증: Authorization: KakaoAK
 * {REST_API_KEY}
 */
@Configuration
@RequiredArgsConstructor
public class KakaoLocalConfig {

    private final KakaoApiProperties props;

    @Bean
    public WebClient kakaoLocalWebClient() {
        // 필요 시 큰 응답 대비 버퍼 상향 (기본 256KB)
        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(cfg -> cfg.defaultCodecs().maxInMemorySize(2 * 1024 * 1024)) // 2MB
            .build();

        return WebClient.builder()
            .baseUrl(props.getBaseUrl()) // https://dapi.kakao.com
            .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + props.getKey())
            .exchangeStrategies(strategies)
            .build();
    }
}
