package com.gbg.hubservice.infrastructure.config;

import com.gbg.hubservice.infrastructure.config.filter.HeaderAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final HeaderAuthFilter headerAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
        DefaultAuthenticationEventPublisher publisher) throws Exception {

        http
            //  JWT 기반 인증 사용 → CSRF 비활성화
            .csrf(AbstractHttpConfigurer::disable)

            //  세션 미사용 (Stateless)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 권한 규칙 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/v1/hubs/**").hasRole("MASTER")
                .requestMatchers(HttpMethod.PUT, "/v1/hubs/**").hasRole("MASTER")
                .requestMatchers(HttpMethod.DELETE, "/v1/hubs/**").hasRole("MASTER")

                // 그 외 요청은 인증만 되면 접근 가능
                .anyRequest().authenticated()
            )

            //  게이트웨이에서 전달한 헤더(X-Auth-Id, X-Auth-Role)로 인증 복원
            .addFilterBefore(headerAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
