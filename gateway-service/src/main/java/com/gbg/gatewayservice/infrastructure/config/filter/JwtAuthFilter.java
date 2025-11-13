package com.gbg.gatewayservice.infrastructure.config.filter;

import com.gbg.gatewayservice.application.service.AuthService;
import com.gbg.gatewayservice.domain.entity.UserStatus;
import com.gbg.gatewayservice.infrastructure.config.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GlobalFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;
    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        // 토큰 없이 접근 가능한 경로 허용
        String path = request.getURI().getPath();
        if (path.startsWith("/v1/users/sign-up") || path.startsWith("/v1/users/sign-in")) {
            return chain.filter(exchange);
        }

        // Authorization 헤더 확인
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "Missing or invalid Authorization header",
                HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.substring(7);

        if (Boolean.TRUE.equals(redisTemplate.hasKey("BL:" + token))) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }


        // Jwt 유효성 검증
        if (!jwtTokenProvider.validateToken(token)) {
            return onError(exchange, "Invalid JWT Token", HttpStatus.UNAUTHORIZED);
        }

        // Claims 추출 (userId, role)
        Claims claims = jwtTokenProvider.getClaims(token);
        UUID userId = UUID.fromString(claims.getSubject());
        String role = claims.get("roles", String.class);
        log.info("userId : {}, role: {}", userId, role);

        return authService.getUserStatus(userId)
            .flatMap(status -> {
                if (status == UserStatus.PENDING && !path.startsWith("/v1/users")) {
                    return onError(exchange, "User status is PENDING", HttpStatus.FORBIDDEN);
                }
                // 헤더 추가
                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-Auth-Id", userId.toString())
                    .header("X-Auth-Role", role)
                    .build();

                log.info("[X-Auth-Id] : {} ", modifiedRequest.getHeaders().getFirst("X-Auth-Id"));
                log.info("[X-Auth-Role] : {} ",
                    modifiedRequest.getHeaders().getFirst("X-Auth-Role"));

                return chain.filter(exchange.mutate().request(modifiedRequest).build());

            })
            .onErrorResume(throwable -> {
                if (throwable instanceof WebClientResponseException e) {
                    HttpStatusCode code = e.getStatusCode();
                    HttpStatus status = HttpStatus.resolve(code.value());

                    if (status == HttpStatus.FORBIDDEN) {
                        return onError(exchange, "User service returned FORBIDDEN",
                            HttpStatus.FORBIDDEN);
                    }

                    if (status == HttpStatus.NOT_FOUND) {
                        return onError(exchange, "User not found", HttpStatus.NOT_FOUND);
                    }

                    HttpStatus fallback =
                        (status != null) ? status : HttpStatus.INTERNAL_SERVER_ERROR;
                    return onError(exchange, "User service responded with error", fallback);
                }

                return onError(exchange, "Gateway internal error",
                    HttpStatus.INTERNAL_SERVER_ERROR);
            });

    }


    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }
}
