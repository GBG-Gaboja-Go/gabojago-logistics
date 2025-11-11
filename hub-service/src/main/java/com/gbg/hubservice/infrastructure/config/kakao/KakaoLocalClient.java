package com.gbg.hubservice.infrastructure.config.kakao;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

/**
 * 카카오 로컬(지오코딩) 클라이언트 - GET /v2/local/search/address.json?query=... - 응답: x=경도(lon), y=위도(lat)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoLocalClient {

    private final WebClient kakaoLocalWebClient;

    /**
     * 주소 → 좌표 (동기)
     */
    public GeoPoint geocodeAddress(String address) {
        return geocodeAddressAsync(address).block();
    }

    /**
     * 주소 → 좌표 (비동기)
     */
    public Mono<GeoPoint> geocodeAddressAsync(String address) {
        if (address == null || address.isBlank()) {
            return Mono.error(new IllegalArgumentException("주소가 비어 있습니다."));
        }

        return kakaoLocalWebClient.get()
            .uri(uri -> uri.path("/v2/local/search/address.json")
                .queryParam("query", address)
                .build())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, r ->
                r.bodyToMono(String.class).defaultIfEmpty("")
                    .flatMap(body -> {
                        log.warn("[Kakao] 4xx status={} body={}", r.statusCode(), body);
                        String statusText = (r.statusCode() instanceof HttpStatus hs)
                            ? hs.getReasonPhrase()
                            : r.statusCode().toString();
                        return Mono.error(new WebClientResponseException(
                            "Kakao 4xx: " + body,
                            r.statusCode().value(),
                            statusText,
                            r.headers().asHttpHeaders(),
                            body.getBytes(StandardCharsets.UTF_8),
                            StandardCharsets.UTF_8
                        ));
                    }))
            .onStatus(HttpStatusCode::is5xxServerError, r ->
                r.bodyToMono(String.class).defaultIfEmpty("")
                    .flatMap(body -> {
                        log.error("[Kakao] 5xx status={} body={}", r.statusCode(), body);
                        String statusText = (r.statusCode() instanceof HttpStatus hs)
                            ? hs.getReasonPhrase()
                            : r.statusCode().toString();
                        return Mono.error(new WebClientResponseException(
                            "Kakao 5xx: " + body,
                            r.statusCode().value(),
                            statusText,
                            r.headers().asHttpHeaders(),
                            body.getBytes(StandardCharsets.UTF_8),
                            StandardCharsets.UTF_8
                        ));
                    }))
            .bodyToMono(KakaoAddressResponse.class)
            .retryWhen(
                Retry.backoff(3, Duration.ofMillis(300))
                    .filter(ex -> ex instanceof WebClientResponseException w
                        && w.getStatusCode().value() == 429)
                    .onRetryExhaustedThrow((spec, sig) -> sig.failure())
            )
            .map(resp -> {
                if (resp == null || !resp.hasResult()) {
                    throw new IllegalStateException("주소 검색 결과가 없습니다: " + address);
                }
                KakaoAddressResponse.Document doc = resp.first();
                double lat = Double.parseDouble(doc.y()); // 위도
                double lon = Double.parseDouble(doc.x()); // 경도
                return new GeoPoint(lat, lon);
            })
            .doOnSuccess(
                p -> log.info("[Kakao] geocode OK: '{}' -> lat={}, lon={}", address, p.lat(),
                    p.lon()))
            .doOnError(e -> log.error("[Kakao] geocode FAIL: '{}' -> {}", address, e.getMessage()));
    }

    /**
     * 최소 필드만 쓰는 응답 DTO (필요시 별도 파일로 분리 가능)
     */
    public record KakaoAddressResponse(List<Document> documents, Meta meta) {

        public boolean hasResult() {
            return documents != null && !documents.isEmpty();
        }

        public Document first() {
            return documents.get(0);
        }

        public record Document(String x, String y) {

        } // x=lon, y=lat

        public record Meta(int total_count) {

        }
    }

    /**
     * 반환 값 객체
     */
    public record GeoPoint(double lat, double lon) {

    }
}
