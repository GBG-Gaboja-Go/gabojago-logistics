package com.gbg.vendorservice.application.service.impl;

import com.gabojago.exception.AppException;
import com.gbg.vendorservice.application.service.VendorService;
import com.gbg.vendorservice.domain.entity.Vendor;
import com.gbg.vendorservice.domain.repository.VendorRepository;
import com.gbg.vendorservice.infrastructure.client.HubClient;
import com.gbg.vendorservice.infrastructure.client.UserClient;
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
    private final HubClient hubClient;
    private final UserClient userClient;

    @Override
    @Transactional
    public CreateVendorResponseDto createVendor(CustomUser customUser, CreateVendorRequestDto dto) {

        UUID vendorManagerId = dto.getVendorManagerId();

        // 허브 존재 검증
        if (!hubClient.existsById(dto.getHubId())) {
            throw new AppException(VendorErrorCode.HUB_NOT_FOUND);
        }

        // 유저 존재 검증
        if (!userClient.existsById(vendorManagerId)) {
            throw new AppException(VendorErrorCode.USER_NOT_FOUND);
        }

        // 비즈니스 검증: 공급자/수령자 중 하나만 true여야 함
        boolean supplier = Boolean.TRUE.equals(dto.getSupplier());
        boolean receiver = Boolean.TRUE.equals(dto.getReceiver());

        if (!(supplier ^ receiver)) { // XOR — 둘 중 하나만 true여야 함
            throw new AppException(VendorErrorCode.INVALID_VENDOR_TYPE);
        }

        Vendor vendor = Vendor.builder()
            .name(dto.getName())
            .hubId(dto.getHubId())
            .vendorManagerId(vendorManagerId) // 업체 매니저 ID
            .address(dto.getAddress())
            .supplier(dto.getSupplier())
            .receiver(dto.getReceiver())
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
    public List<VendorResponseDto> getVendorsByVendorManagerId(UUID managerId) {
        List<Vendor> vendors = vendorRepository.findAllByVendorManagerId(managerId);
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
    public List<VendorResponseDto> getVendorsByHubId(UUID hubId) {
        return vendorRepository.findByHubId(hubId).stream()
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

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID vendorId) {
        return vendorRepository.existsById(vendorId);
    }
}
