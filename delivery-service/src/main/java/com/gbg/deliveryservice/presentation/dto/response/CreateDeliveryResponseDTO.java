package com.gbg.deliveryservice.presentation.dto.response;

import com.gbg.deliveryservice.domain.entity.Delivery;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
public record CreateDeliveryResponseDTO(
    DeliveryDTO delivery
) {

    public static CreateDeliveryResponseDTO from(Delivery delivery) {
        return CreateDeliveryResponseDTO.builder()
            .delivery(DeliveryDTO.from(delivery))
            .build();
    }

    @Getter
    @Builder
    public static class DeliveryDTO {

        private final UUID id;

        public static DeliveryDTO from(Delivery delivery) {
            return DeliveryDTO.builder()
                .id(delivery.getId())
                .build();
        }
    }

}
