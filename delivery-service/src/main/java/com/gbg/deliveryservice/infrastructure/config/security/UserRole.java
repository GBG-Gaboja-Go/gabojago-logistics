package com.gbg.deliveryservice.infrastructure.config.security;

import lombok.Getter;

@Getter
public enum UserRole {

    MASTER("ROLE_MASTER"),
    HUB_MANAGER("ROLE_HUB_MANAGER"),
    DELIVERY_MANAGER("ROLE_DELIVERY_MANAGER"),
    VENDOR_MANAGER("ROLE_VENDOR_MANAGER"),
    USER("ROLE_USER");

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public boolean isAdmin() {
        return this == MASTER;
    }

}
