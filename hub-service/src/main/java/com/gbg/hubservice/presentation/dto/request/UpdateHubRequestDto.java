package com.gbg.hubservice.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHubRequestDto {

    @NotNull(message = "허브 정보를 입력해주세요.")
    @Valid
    private HubDto hub;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubDto {

        @NotBlank(message = "허브 이름을 입력해주세요.")
        @Schema(example = "서울특별시 센터")
        private String name;

        @NotBlank(message = "주소를 입력해주세요.")
        @Schema(example = "서울특별시 송파구 송파대로 55")
        private String address;

        @DecimalMin(value = "-90.0", message = "위도는 -90 ~ 90 범위여야 합니다.", inclusive = true)
        @DecimalMax(value = "90.0", message = "위도는 -90 ~ 90 범위여야 합니다.", inclusive = true)
        private BigDecimal latitude;

        @DecimalMin(value = "-180.0", message = "경도는 -180 ~ 180 범위여야 합니다.", inclusive = true)
        @DecimalMax(value = "180.0", message = "경도는 -180 ~ 180 범위여야 합니다.", inclusive = true)
        private BigDecimal longitude;
    }
}
