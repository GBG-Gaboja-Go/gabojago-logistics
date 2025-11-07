package com.gbg.hubservice.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

        @NotNull(message = "거리(m)를 입력해주세요.")
        @Min(value = 0, message = "거리는 0 이상이어야 합니다.")
        private Integer distance;
    }
}
