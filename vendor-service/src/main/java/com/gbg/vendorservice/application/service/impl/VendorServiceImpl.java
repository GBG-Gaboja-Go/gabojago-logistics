package com.gbg.vendorservice.application.service.impl;

import com.gabojago.exception.AppException;
import com.gbg.vendorservice.application.service.VendorService;
import com.gbg.vendorservice.domain.entity.Vendor;
import com.gbg.vendorservice.domain.repository.VendorRepository;
import com.gbg.vendorservice.infrastructure.config.auth.CustomUser;
import com.gbg.vendorservice.presentation.advice.VendorErrorCode;
import com.gbg.vendorservice.presentation.dto.request.CreateVendorRequestDto;
import com.gbg.vendorservice.presentation.dto.request.SearchVendorRequestDto;
import com.gbg.vendorservice.presentation.dto.request.UpdateVendorRequestDto;
import com.gbg.vendorservice.presentation.dto.response.CreateVendorResponseDto;
import com.gbg.vendorservice.presentation.dto.response.SearchVendorResponseDto;
import com.gbg.vendorservice.presentation.dto.response.VendorResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Override
    @Transactional
    public CreateVendorResponseDto createVendor(CustomUser customUser, CreateVendorRequestDto dto) {
        Vendor vendor = Vendor.builder()
            .name(dto.getName())
            .hubId(dto.getHubId())
            .managerId(UUID.fromString(customUser.getUserId())) // 생성자 ID
            .address(dto.getAddress())
            .isSupplier(dto.getIsSupplier())
            .isReceiver(dto.getIsReceiver())
            .isDeleted(false)
            .build();

        vendorRepository.save(vendor);
        return CreateVendorResponseDto.from(vendor);
    }

    @Override
    @Transactional(readOnly = true)
    public VendorResponseDto getVendors(UUID vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
            .orElseThrow(() -> new AppException(VendorErrorCode.VENDOR_NOT_FOUND));
        return VendorResponseDto.from(vendor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendorResponseDto> getVendorsByManagerId(UUID managerId) {
        List<Vendor> vendors = vendorRepository.findAllByManagerId(managerId);
        if (vendors.isEmpty()) {
            throw new AppException(VendorErrorCode.VENDOR_NOT_FOUND);
        }
        return vendors.stream()
            .map(VendorResponseDto::from)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendorResponseDto> getAllVendors() {
        return vendorRepository.findAll().stream()
            .map(VendorResponseDto::from)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SearchVendorResponseDto searchVendors(SearchVendorRequestDto dto, Pageable pageable) {
        Page<Vendor> vendorPage = vendorRepository.search(dto, pageable);
        return SearchVendorResponseDto.from(vendorPage);
    }

    @Override
    @Transactional
    public VendorResponseDto updateVendor(UUID vendorId, UpdateVendorRequestDto dto) {
        Vendor vendor = vendorRepository.findById(vendorId)
            .orElseThrow(() -> new AppException(VendorErrorCode.VENDOR_NOT_FOUND));

        vendor.update(dto);
        vendorRepository.save(vendor);

        return VendorResponseDto.from(vendor);
    }

    @Override
    @Transactional
    public void deleteVendor(UUID vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
            .orElseThrow(() -> new AppException(VendorErrorCode.VENDOR_NOT_FOUND));
        vendorRepository.delete(vendor);
    }
}
