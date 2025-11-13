package com.gbg.vendorservice.presentation.dto.response;

import com.gbg.vendorservice.domain.entity.Vendor;
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
        private boolean supplier;
        private boolean receiver;
        private String address;
    }

    public static VendorResponseDto from(Vendor vendor) {
        return VendorResponseDto.builder()
            .vendor(
                VendorDto.builder()
                    .id(vendor.getId())
                    .name(vendor.getName())
                    .hubId(vendor.getHubId())
                    .vendorManagerId(vendor.getVendorManagerId())
                    .supplier(vendor.getSupplier())
                    .receiver(vendor.getReceiver())
                    .address(vendor.getAddress())
                    .build()
            )
            .build();

    }
}
