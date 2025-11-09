package com.gbg.deliveryservice.presentation.dto.response;

import com.gbg.deliveryservice.domain.entity.Delivery;
import com.gbg.deliveryservice.domain.entity.enums.DeliveryStatus;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
public record GetMyDeliveryResponseDTO(
    Page<DeliveryDTO> deliveries
) {

    @Builder
    public static GetMyDeliveryResponseDTO from(
        Page<Delivery> deliveries
    ) {

        return GetMyDeliveryResponseDTO.builder()
            .deliveries(deliveries.map(DeliveryDTO::from))
            .build();
    }

    @Getter
    @Builder
    public static class DeliveryDTO {

        private final UUID id;
        private final UUID orderId;
        private final String deliveryAddress;
        private final double actualDistance;
        private final LocalTime actualDeliveryTime;
        private final double estimatedDistance;
        private final LocalTime estimatedTime;
        private final DeliveryStatus status;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;
        private LocalDateTime startedTime;
        private LocalDateTime completedTime;

        public static DeliveryDTO from(Delivery delivery) {
            return DeliveryDTO.builder()
                .id(delivery.getId())
                .orderId(delivery.getOrderId())
                .deliveryAddress(delivery.getDeliveryAddress())
                .actualDistance(delivery.getActualDistance())
                .actualDeliveryTime(delivery.getActualDeliveryTime())
                .estimatedDistance(delivery.getEstimatedDistance())
                .estimatedTime(delivery.getEstimatedTime())
                .status(delivery.getStatus())
                .createdAt(delivery.getCreatedAt())
                .updatedAt(delivery.getUpdatedAt())
                .startedTime(delivery.getStartedTime())
                .completedTime(delivery.getCompletedTime())
                .build();
        }
    }

}
