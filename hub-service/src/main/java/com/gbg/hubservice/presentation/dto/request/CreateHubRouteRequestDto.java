package com.gbg.hubservice.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateHubRouteRequestDto {

    @NotNull(message = "허브 경로 정보를 입력해주세요.")
    @Valid
    private RouteDto route;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteDto {

        @NotNull(message = "출발 허브 ID를 입력해주세요.")
        private UUID startHubId;

        @NotNull(message = "도착 허브 ID를 입력해주세요.")
        private UUID endHubId;

        @PositiveOrZero(message = "거리는 0 이상이어야 합니다.")
        private Double distance;

        @PositiveOrZero(message = "소요시간은 0분 이상이어야 합니다.")
        private Integer duration;
    }
}
