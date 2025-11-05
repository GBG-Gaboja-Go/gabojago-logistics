package com.gbg.deliveryservice.presentation.dto.response;

import com.gbg.deliveryservice.domain.DeliveryStatus;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
public record GetMyDeliveryResponseDTO(
    List<DeliveryDTO> deliveries,
    PageInfo pageInfo
) {

    @Builder
    public static GetMyDeliveryResponseDTO from(
        List<DeliveryDTO> deliveries,
        PageInfo pageInfo
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
        private final double estimatedDistance;
        private final LocalTime estimatedTime;
        private final DeliveryStatus status;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        public static DeliveryDTO from(DeliveryDTO delivery) {
            return DeliveryDTO.builder()
                .id(delivery.getId())
                .orderId(delivery.getOrderId())
                .deliveryAddress(delivery.getDeliveryAddress())
                .estimatedDistance(delivery.getEstimatedDistance())
                .estimatedTime(delivery.getEstimatedTime())
                .status(delivery.getStatus())
                .createdAt(delivery.getCreatedAt())
                .updatedAt(delivery.getUpdatedAt())
                .build();
        }
    }

    @Getter
    @Builder
    public static class PageInfo {

        private int pageNumber;
        private int pageSize;
        private int totalPages;
        private long totalElements;

        public static PageInfo from(
            int pageNumber,
            int pageSize,
            int totalPages,
            long totalElements
        ) {
            return PageInfo.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
        }
    }
}