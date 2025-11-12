package com.gbg.deliveryservice.infrastructure.client.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorResponseDto {

    private VendorDto vendor;

    @Getter
    @Builder
    public static class VendorDto {

        private UUID id;
        private String name;
        private UUID hubId;
        private UUID vendorManagerId;
        private boolean isSupplier;
        private boolean isReceiver;
        private String address;
    }
}
