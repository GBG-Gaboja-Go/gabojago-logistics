package com.gbg.orderservice.presentation.dto.response;

import java.math.BigDecimal;
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
public class GetHubResponseDto {

    private HubDto hub;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubDto {

        private UUID id;
        private String name;
        private String address;
        private UUID userId;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
