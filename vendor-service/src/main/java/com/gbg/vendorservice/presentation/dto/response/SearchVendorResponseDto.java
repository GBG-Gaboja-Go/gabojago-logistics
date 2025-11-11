package com.gbg.vendorservice.presentation.dto.response;

import com.gbg.vendorservice.domain.entity.Vendor;
import com.gbg.vendorservice.presentation.dto.response.VendorResponseDto.VendorDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchVendorResponseDto {

    private List<VendorDto> vendors;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    
    public static SearchVendorResponseDto from(Page<Vendor> vendorPage) {
        List<VendorResponseDto.VendorDto> vendorDtos = vendorPage.getContent().stream()
            .map(vendor -> VendorResponseDto.VendorDto.builder()
                .id(vendor.getId())
                .name(vendor.getName())
                .hubId(vendor.getHubId())
                .managerId(vendor.getManagerId())
                .isSupplier(vendor.getIsSupplier())
                .isReceiver(vendor.getIsReceiver())
                .address(vendor.getAddress())
                .build())
            .collect(Collectors.toList());

        return SearchVendorResponseDto.builder()
            .vendors(vendorDtos)
            .totalElements(vendorPage.getTotalElements())
            .totalPages(vendorPage.getTotalPages())
            .currentPage(vendorPage.getNumber())
            .build();
    }
}
