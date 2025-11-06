package com.gbg.hubservice.presentation.dto.response;

import java.math.BigDecimal;
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
public class UpdateHubResponseDto {

    private HubDto hub;

    public static UpdateHubResponseDto of(
        UUID id,
        String name,
        String address,
        BigDecimal latitude,
        BigDecimal longitude
    ) {
        return UpdateHubResponseDto.builder()
            .hub(HubDto.builder()
                .id(id)
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .updatedAt(Instant.now())
                .build())
            .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubDto {

        private UUID id;
        private String name;
        private String address;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private Instant updatedAt;
    }
}
