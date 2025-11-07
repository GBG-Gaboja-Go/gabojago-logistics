package com.gbg.hubservice.presentation.dto.response;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetHubRouteResponseDto {

    private RouteDto route;

    public static GetHubRouteResponseDto of(UUID id,
        UUID startHubId,
        UUID endHubId,
        Integer distance) {
        Instant now = Instant.now();
        return GetHubRouteResponseDto.builder()
            .route(RouteDto.builder()
                .id(id)
                .startHubId(startHubId)
                .endHubId(endHubId)
                .distance(distance)
                .createdAt(now)
                .updatedAt(now)
                .build())
            .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteDto {

        private UUID id;
        private UUID startHubId;
        private UUID endHubId;
        private Integer distance;
        private Instant createdAt;
        private Instant updatedAt;
    }
}
