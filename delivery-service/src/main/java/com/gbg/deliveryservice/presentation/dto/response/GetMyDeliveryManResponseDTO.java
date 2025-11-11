package com.gbg.deliveryservice.presentation.dto.response;

import com.gbg.deliveryservice.domain.entity.DeliveryMan;
import com.gbg.deliveryservice.domain.entity.enums.DeliveryType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
public record GetMyDeliveryManResponseDTO(
    DeliveryManDto delivery
) {

    public static GetMyDeliveryManResponseDTO from(DeliveryMan deliveryMan) {
        return GetMyDeliveryManResponseDTO.builder()
            .delivery(DeliveryManDto.from(deliveryMan))
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

        public static DeliveryManDto from(DeliveryMan deliveryMan) {
            return DeliveryManDto.builder()
                .id(deliveryMan.getId())
                .hubId(deliveryMan.getHubId())
                .type(deliveryMan.getType())
                .sequence(deliveryMan.getSequence())
                .createdAt(deliveryMan.getCreatedAt())
                .updatedAt(deliveryMan.getUpdatedAt())
                .build();
        }
    }

}
