package com.gbg.vendorservice.presentation.dto.response;

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

    private UUID id;
    private String name;
    private UUID hubId;
    private UUID managerId;
    private boolean isSupplier;
    private boolean isReceiver;
    private String address;
}
