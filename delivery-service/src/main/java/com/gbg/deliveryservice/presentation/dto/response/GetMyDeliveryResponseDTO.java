package com.gbg.deliveryservice.presentation.dto.response;

import com.gbg.deliveryservice.domain.enums.DeliveryStatus;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
public record GetMyDeliveryResponseDTO(
    List<DeliveryDTO> deliveries,
    PageInfoDTO pageInfo
) {

    @Builder
    public static GetMyDeliveryResponseDTO from(
        List<DeliveryDTO> deliveries,
        PageInfoDTO pageInfo
    ) {

        return GetMyDeliveryResponseDTO.builder()
            .deliveries(deliveries)
            .pageInfo(pageInfo)
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
        private LocalDateTime startedAt;
        private LocalDateTime completedAt;

        public static DeliveryDTO from(DeliveryDTO delivery) {
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
                .startedAt(delivery.getStartedAt())
                .completedAt(delivery.getCompletedAt())
                .build();
        }
    }

}