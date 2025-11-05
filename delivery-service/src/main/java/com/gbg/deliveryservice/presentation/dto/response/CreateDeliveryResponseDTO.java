package com.gbg.deliveryservice.presentation.dto.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
public record CreateDeliveryResponseDTO(
    DeliveryDTO delivery
) {

    public static CreateDeliveryResponseDTO from(UUID id) {
        return CreateDeliveryResponseDTO.builder()
            .delivery(DeliveryDTO.from(id))
            .build();
    }

    @Getter
    @Builder
    public static class DeliveryDTO {

        private final UUID id;

        public static DeliveryDTO from(UUID id) {
            return DeliveryDTO.builder()
                .id(id)
                .build();
        }
    }

}
