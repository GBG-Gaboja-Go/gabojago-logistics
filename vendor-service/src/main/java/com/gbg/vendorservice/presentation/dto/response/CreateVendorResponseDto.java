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
public class CreateVendorResponseDto {

    private VendorDto vendor;

    @Getter
    @Builder
    public static class VendorDto {

        private UUID id;
    }

    public static CreateVendorResponseDto from(Vendor vendor) {
        return CreateVendorResponseDto.builder()
            .vendor(
                VendorDto.builder()
                    .id(vendor.getId())
                    .build()
            )
            .build();
    }
}
