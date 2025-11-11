package com.gbg.hubservice.global.config;

import com.gbg.hubservice.infrastructure.kakao.KakaoApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties({
    KakaoApiProperties.class
})
public class EnableProps {

}
