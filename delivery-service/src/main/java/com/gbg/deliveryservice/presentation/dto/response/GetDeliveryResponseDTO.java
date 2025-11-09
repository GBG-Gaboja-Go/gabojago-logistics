package com.gbg.deliveryservice.presentation.dto.response;

import com.gbg.deliveryservice.domain.entity.Delivery;
import com.gbg.deliveryservice.domain.entity.HubDelivery;
import com.gbg.deliveryservice.domain.entity.VendorDelivery;
import com.gbg.deliveryservice.domain.entity.enums.DeliveryStatus;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
public record GetDeliveryResponseDTO(
    DeliveryDTO delivery,
    HubDeliveryDTO hubDelivery,
    VendorDeliveryDTO vendorDelivery
) {

    public static GetDeliveryResponseDTO from(Delivery delivery, HubDelivery hubDelivery,
        VendorDelivery vendorDelivery) {
        return GetDeliveryResponseDTO.builder()
            .delivery(DeliveryDTO.from(delivery))
            .hubDelivery(HubDeliveryDTO.from(hubDelivery))
            .vendorDelivery(VendorDeliveryDTO.from(vendorDelivery))
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

        public static DeliveryDTO from(
            Delivery delivery) {
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

    @Getter
    @Builder
    public static class HubDeliveryDTO {

        private final UUID id;
        private final UUID hubToId;
        private final UUID hubFromId;
        private final UUID deliverymanId;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        public static HubDeliveryDTO from(
            HubDelivery hubDelivery) {
            return HubDeliveryDTO.builder()
                .id(hubDelivery.getId())
                .hubToId(hubDelivery.getHubToId())
                .hubFromId(hubDelivery.getHubFromId())
                .createdAt(hubDelivery.getCreatedAt())
                .updatedAt(hubDelivery.getUpdatedAt())
                .build();
        }
    }


    @Getter
    @Builder
    public static class VendorDeliveryDTO {

        private final UUID id;
        private final UUID userToId;
        private final UUID userFromId;
        private final UUID deliverymanId;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        public static VendorDeliveryDTO from(
            VendorDelivery vendorDelivery) {
            return VendorDeliveryDTO.builder()
                .id(vendorDelivery.getId())
                .userToId(vendorDelivery.getUserToId())
                .userFromId(vendorDelivery.getUserFromId())
                .createdAt(vendorDelivery.getCreatedAt())
                .updatedAt(vendorDelivery.getUpdatedAt())
                .build();
        }
    }

}
