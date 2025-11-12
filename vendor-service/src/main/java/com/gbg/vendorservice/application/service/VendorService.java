package com.gbg.vendorservice.application.service;

import com.gbg.vendorservice.infrastructure.config.auth.CustomUser;
import com.gbg.vendorservice.presentation.dto.request.CreateVendorRequestDto;
import com.gbg.vendorservice.presentation.dto.request.SearchVendorRequestDto;
import com.gbg.vendorservice.presentation.dto.request.UpdateVendorRequestDto;
import com.gbg.vendorservice.presentation.dto.response.CreateVendorResponseDto;
import com.gbg.vendorservice.presentation.dto.response.SearchVendorResponseDto;
import com.gbg.vendorservice.presentation.dto.response.VendorResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface VendorService {

    CreateVendorResponseDto createVendor(CustomUser customUser, CreateVendorRequestDto dto);

    VendorResponseDto getVendors(UUID vendorId);

    List<VendorResponseDto> getAllVendors();

    SearchVendorResponseDto searchVendors(SearchVendorRequestDto dto, Pageable pageable);

    List<VendorResponseDto> getVendorsByVendorManagerId(UUID managerId);

    VendorResponseDto updateVendor(UUID vendorId, UpdateVendorRequestDto dto);

    void deleteVendor(UUID vendorId);

    boolean existsById(UUID vendorId);
}
