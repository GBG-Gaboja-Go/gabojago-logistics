package com.gbg.hubservice.presentation.dto.request;

import jakarta.validation.Valid;
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
public class CreateHubRequestDto {

    @NotNull(message = "허브 정보를 입력해주세요.")
    @Valid
    private HubDto hub;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubDto {

        @NotBlank(message = "허브 이름을 입력해주세요.")
        private String name;

        @NotBlank(message = "주소를 입력해주세요.")
        private String address;

        @NotNull(message = "위도를 입력해주세요.")
        private BigDecimal latitude;

        @NotNull(message = "경도를 입력해주세요.")
        private BigDecimal longitude;
    }
}