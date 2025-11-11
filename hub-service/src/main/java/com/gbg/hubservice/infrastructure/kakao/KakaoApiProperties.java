package com.gbg.hubservice.infrastructure.kakao;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

/**
 * kakao.api.* 프로퍼티 바인딩
 * <p>
 * application.yml: kakao: api: key: ${KAKAO_REST_API_KEY} base-url: https://dapi.kakao.com
 */
@Getter
@ConfigurationProperties(prefix = "kakao.api")
public class KakaoApiProperties {

    private final String key;       // REST API Key
    private final String baseUrl;   // https://dapi.kakao.com

    @ConstructorBinding
    public KakaoApiProperties(String key, String baseUrl) {
        this.key = key;
        this.baseUrl = baseUrl;
    }
}
