package com.gbg.hubservice.presentation.dto.response;

import com.gbg.hubservice.domain.entity.HubRoute;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHubRouteResponseDto {

    private RouteDto route;

    public static UpdateHubRouteResponseDto of(
        UUID id,
        UUID startHubId,
        UUID endHubId,
        Double distance,
        Integer duration
    ) {
        return UpdateHubRouteResponseDto.builder()
            .route(RouteDto.builder()
                .id(id)
                .startHubId(startHubId)
                .endHubId(endHubId)
                .distance(distance)
                .duration(duration)
                .updatedAt(LocalDateTime.now())
                .build())
            .build();
    }

    public static UpdateHubRouteResponseDto of(HubRoute route) {
        return UpdateHubRouteResponseDto.builder()
            .route(RouteDto.builder()
                .id(route.getId())
                .startHubId(route.getStartHubId())
                .endHubId(route.getEndHubId())
                .distance(route.getDistance())
                .duration(route.getDuration())
                .updatedAt(route.getUpdatedAt())
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
        private Double distance;
        private Integer duration;
        private LocalDateTime updatedAt;
    }
}
