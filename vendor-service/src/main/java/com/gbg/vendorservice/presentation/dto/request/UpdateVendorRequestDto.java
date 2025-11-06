package com.gbg.vendorservice.presentation.dto.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVendorRequestDto {

    private String name;
    private UUID hubId;
    private UUID managerId;
    private String address;

    private Boolean isSupplier;
    private Boolean isReceiver;
}
