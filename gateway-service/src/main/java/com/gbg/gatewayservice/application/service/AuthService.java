package com.gbg.gatewayservice.application.service;

import com.gbg.gatewayservice.domain.entity.UserStatus;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final WebClient.Builder webClientBuilder;

    private record BaseResponseDto<T>(
        String message,
        T data,
        String status
    ) {}

    public Mono<UserStatus> getUserStatus(UUID userId) {
        return webClientBuilder.build()
            .get()
            .uri("http://user-service/v1/users/internal/{userId}/status", userId)
            .retrieve()
            .bodyToMono(BaseResponseDto.class)
            .map(response -> {
                Object data = ((BaseResponseDto<?>) response).data();
                return UserStatus.valueOf(data.toString());
            })
            .doOnError(e ->
                System.err.println("[User-service error] : " + e.getMessage())
                );
    }

}
