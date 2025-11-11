package com.gbg.deliveryservice.presentation.dto.response;

import com.gbg.deliveryservice.domain.entity.DeliveryMan;
import com.gbg.deliveryservice.domain.entity.enums.DeliveryType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
public record GetDeliveryManResponseDTO(
    DeliveryManDto delivery
) {

    public static GetDeliveryManResponseDTO from(DeliveryMan deliveryman) {
        return GetDeliveryManResponseDTO.builder()
            .delivery(DeliveryManDto.from(deliveryman))
            .build();
    }

    @Getter
    @Builder
    public static class DeliveryManDto {

        private final UUID id;
        private UUID hubId;
        private DeliveryType type;
        private int sequence;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        public static DeliveryManDto from(DeliveryMan deliveryman) {
            return DeliveryManDto.builder()
                .id(deliveryman.getId())
                .hubId(deliveryman.getHubId())
                .type(deliveryman.getType())
                .sequence(deliveryman.getSequence())
                .createdAt(deliveryman.getCreatedAt())
                .updatedAt(deliveryman.getUpdatedAt())
                .build();
        }
    }

}
