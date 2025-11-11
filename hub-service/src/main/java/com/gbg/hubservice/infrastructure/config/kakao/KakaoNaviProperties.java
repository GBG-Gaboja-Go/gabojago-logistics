package com.gbg.hubservice.infrastructure.config.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao.navi")
public record KakaoNaviProperties(String baseUrl) {

}
