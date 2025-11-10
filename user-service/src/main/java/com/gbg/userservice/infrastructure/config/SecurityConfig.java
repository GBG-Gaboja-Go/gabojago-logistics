package com.gbg.userservice.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbg.userservice.domain.repository.UserRepository;
import com.gbg.userservice.infrastructure.config.auth.CustomAccessDeniedHandler;
import com.gbg.userservice.infrastructure.config.auth.CustomAuthenticationEntryPoint;
import com.gbg.userservice.infrastructure.config.filter.AuthenticationFilter;
import com.gbg.userservice.infrastructure.config.filter.JwtAuthorizationFilter;
import com.gbg.userservice.infrastructure.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationFilter filter = new AuthenticationFilter(jwtTokenProvider, objectMapper, userRepository);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationFilter filter) throws Exception {
        System.out.println("SecurityFilterChain 등록 시작");

        return http
            // JWT는 게이트웨이에서 이미 검증되므로 세션/폼로그인 비활성화
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 인증 & 인가 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v1/users/sign-up").permitAll()
                .requestMatchers("/v1/users/sign-in").permitAll()
                .requestMatchers("/v1/users/internal/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-resources/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .anyRequest().authenticated())

            // 예외 핸들러 (공통 모듈에서 가져온 클래스)
            .exceptionHandling(ex -> ex
                .accessDeniedHandler(customAccessDeniedHandler)
                .authenticationEntryPoint(customAuthenticationEntryPoint))

            .addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider), AuthenticationFilter.class)

            // 로그인 필터 추가
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)



            .build();
    }


}
