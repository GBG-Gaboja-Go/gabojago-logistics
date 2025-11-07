package com.gbg.deliveryservice.domain.entity.enums;

public enum DeliveryStatus {
    PENDING,
    WAITING_FOR_HUB_DEPARTURE,
    IN_TRANSIT_HUB,
    ARRIVED_AT_HUB,
    OUT_FOR_DELIVERY,
    DELIVERED,
    FAILED,
    CANCELLED,
}
