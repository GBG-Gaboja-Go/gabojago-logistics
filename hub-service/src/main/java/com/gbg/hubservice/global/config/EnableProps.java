// src/main/java/com/gbg/hubservice/global/config/EnableProps.java
package com.gbg.hubservice.global.config;

import com.gbg.hubservice.infrastructure.config.kakao.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    KakaoApiProperties.class,
    KakaoLocalProperties.class,
    KakaoNaviProperties.class
})
public class EnableProps {

}
