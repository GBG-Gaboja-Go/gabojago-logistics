package com.gbg.deliveryservice.presentation.dto.response;

import com.gbg.deliveryservice.domain.entity.enums.DeliveryType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
public record GetDeliveryManResponseDTO(
    DeliveryManDto delivery
) {

    public static GetDeliveryManResponseDTO from(DeliveryManDto deliveryDTO) {
        return GetDeliveryManResponseDTO.builder()
            .delivery(DeliveryManDto.from(deliveryDTO))
            .build();
    }

    @Getter
    @Builder
    public static class DeliveryManDto {

        private final UUID id;
        private UUID hubId;
        private UUID userId;
        private DeliveryType type;
        private int sequence;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        public static DeliveryManDto from(DeliveryManDto delivery) {
            return DeliveryManDto.builder()
                .id(delivery.getId())
                .hubId(delivery.getHubId())
                .userId(delivery.getUserId())
                .type(delivery.getType())
                .sequence(delivery.getSequence())
                .createdAt(delivery.getCreatedAt())
                .updatedAt(delivery.getUpdatedAt())
                .build();
        }
    }

}
