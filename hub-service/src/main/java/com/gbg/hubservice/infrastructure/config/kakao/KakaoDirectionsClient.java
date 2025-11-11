package com.gbg.hubservice.infrastructure.config.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoDirectionsClient {

    private final WebClient kakaoNaviWebClient;
    private final ObjectMapper objectMapper;

    public RouteSummary getSummary(double originLat, double originLon,
        double destLat, double destLon) {
        return getSummaryAsync(originLat, originLon, destLat, destLon).block();
    }

    public Mono<RouteSummary> getSummaryAsync(double originLat, double originLon,
        double destLat, double destLon) {
        String origin = originLon + "," + originLat; // Kakao: lon,lat
        String destination = destLon + "," + destLat;

        return kakaoNaviWebClient.get()
            .uri(u -> u.path("/v1/directions")
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .queryParam("priority", "TIME")
                .build())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, r ->
                r.bodyToMono(String.class).defaultIfEmpty("")
                    .flatMap(body -> {
                        log.warn("[KakaoNavi] 4xx status={} body={}", r.statusCode(), body);
                        String reason = (r.statusCode() instanceof HttpStatus hs)
                            ? hs.getReasonPhrase() : r.statusCode().toString();
                        return Mono.error(new WebClientResponseException(
                            "KakaoNavi 4xx: " + body,
                            r.statusCode().value(),
                            reason,
                            r.headers().asHttpHeaders(),
                            body.getBytes(StandardCharsets.UTF_8),
                            StandardCharsets.UTF_8
                        ));
                    }))
            .onStatus(HttpStatusCode::is5xxServerError, r ->
                r.bodyToMono(String.class).defaultIfEmpty("")
                    .flatMap(body -> {
                        log.error("[KakaoNavi] 5xx status={} body={}", r.statusCode(), body);
                        String reason = (r.statusCode() instanceof HttpStatus hs)
                            ? hs.getReasonPhrase() : r.statusCode().toString();
                        return Mono.error(new WebClientResponseException(
                            "KakaoNavi 5xx: " + body,
                            r.statusCode().value(),
                            reason,
                            r.headers().asHttpHeaders(),
                            body.getBytes(StandardCharsets.UTF_8),
                            StandardCharsets.UTF_8
                        ));
                    }))
            // 1️. RAW 로그로 Kakao 응답 구조 확인
            .bodyToMono(String.class)
            .doOnNext(body -> log.info("[KakaoNavi RAW] {}", body))
            // 2️ JSON 파싱 시 예외 안전 처리
            .flatMap(body -> {
                try {
                    DirectionsResponse resp = objectMapper.readValue(body,
                        DirectionsResponse.class);
                    return Mono.just(resp);
                } catch (JsonProcessingException e) {
                    log.error("[KakaoNavi] JSON 파싱 실패: {}", e.getMessage(), e);
                    return Mono.error(e);
                }
            })
            // 3️. 요약 정보 추출 (summary or sections[0].summary)
            .map(resp -> {
                if (resp == null || resp.routes() == null || resp.routes().isEmpty()) {
                    throw new IllegalStateException("길찾기 결과가 없습니다.");
                }

                var route = resp.routes().get(0);
                Summary sum = Optional.ofNullable(route.summary())
                    .orElseGet(() -> {
                        if (route.sections() != null && !route.sections().isEmpty()) {
                            return route.sections().get(0).summary();
                        }
                        return null;
                    });

                if (sum == null) {
                    throw new IllegalStateException("요약 정보가 없습니다.");
                }

                double km = safeKm(sum.distance());
                int minutes = toMinutes(sum.duration());

                return new RouteSummary(km, minutes);
            })
            .retryWhen(
                Retry.backoff(3, Duration.ofMillis(500))
                    .filter(ex -> ex instanceof WebClientResponseException w
                        && w.getStatusCode().value() == 429)
                    .onRetryExhaustedThrow((spec, sig) -> sig.failure())
            )
            .doOnSuccess(s -> log.info("[KakaoNavi] summary OK: {} km, {} min",
                s.distanceKm(), s.durationMin()))
            .doOnError(e -> log.error("[KakaoNavi] summary FAIL: {}", e.getMessage()));
    }

    private static double safeKm(Long meters) {
        if (meters == null) {
            return 0d;
        }
        return meters / 1000.0;
    }

    private static int toMinutes(Long raw) {
        if (raw == null) {
            return 0;
        }
        double minutes = (raw >= 36000) ? (raw / 60000.0) : (raw / 60.0);
        return (int) Math.round(minutes);
    }

    // DTO
    public record DirectionsResponse(List<Route> routes) {

    }

    public record Route(Summary summary, List<Section> sections) {

    }

    public record Section(Summary summary) {

    }

    public record Summary(Long distance, Long duration) {

    }

    public record RouteSummary(double distanceKm, int durationMin) {

    }
}
