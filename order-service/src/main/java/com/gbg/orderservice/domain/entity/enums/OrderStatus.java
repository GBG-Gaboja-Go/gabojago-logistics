package com.gbg.orderservice.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    DELIVERING("DELIVERING"),
    CANCELED("CANCELED");

    private final String value;
}
