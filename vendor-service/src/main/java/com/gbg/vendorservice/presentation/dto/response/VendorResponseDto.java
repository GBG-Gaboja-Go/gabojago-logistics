package com.gbg.vendorservice.presentation.dto.response;

import java.time.LocalDateTime;
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
    private boolean supplier;
    private boolean receiver;
    private String address;

    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private String deletedBy;
    private LocalDateTime deletedAt;
    private boolean deleted;

}
