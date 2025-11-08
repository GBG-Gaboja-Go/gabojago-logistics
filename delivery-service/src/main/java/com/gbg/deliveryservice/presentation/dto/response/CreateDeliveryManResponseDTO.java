package com.gbg.deliveryservice.presentation.dto.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
public record CreateDeliveryManResponseDTO(
    DeliveryManDTO delivery
) {

    public static CreateDeliveryManResponseDTO from(UUID id) {
        return CreateDeliveryManResponseDTO.builder()
            .delivery(DeliveryManDTO.from(id))
            .build();
    }

    @Getter
    @Builder
    public static class DeliveryManDTO {

        private final UUID id;

        public static DeliveryManDTO from(UUID id) {
            return DeliveryManDTO.builder()
                .id(id)
                .build();
        }
    }

}
