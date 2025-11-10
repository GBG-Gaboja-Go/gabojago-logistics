package com.gbg.orderservice.presentation.dto.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
public record CreateDeliveryResponseDTO(
    DeliveryDTO delivery
) {

    @Getter
    @Builder
    public static class DeliveryDTO {

        private final UUID id;

    }

}
