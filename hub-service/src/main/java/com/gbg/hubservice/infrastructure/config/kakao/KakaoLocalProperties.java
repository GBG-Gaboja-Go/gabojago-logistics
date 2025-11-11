package com.gbg.hubservice.infrastructure.config.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao.local")
public record KakaoLocalProperties(
    String baseUrl
) {

}
