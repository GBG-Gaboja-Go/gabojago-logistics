package com.gbg.deliveryservice.presentation.dto.response;

import com.gbg.deliveryservice.domain.entity.DeliveryMan;
import com.gbg.deliveryservice.domain.entity.enums.DeliveryType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
public record GetDeliveryManPageResponseDTO(
    Page<DeliveryMan> deliveries
) {

    @Builder
    public static GetDeliveryManPageResponseDTO from(
        Page<DeliveryMan> deliveries
    ) {

        return GetDeliveryManPageResponseDTO.builder()
            .deliveries(deliveries)
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

        public static DeliveryManDto from(DeliveryMan delivery) {
            return DeliveryManDto.builder()
                .id(delivery.getId())
                .hubId(delivery.getHubId())
                .type(delivery.getType())
                .sequence(delivery.getSequence())
                .createdAt(delivery.getCreatedAt())
                .updatedAt(delivery.getUpdatedAt())
                .build();
        }
    }

}
