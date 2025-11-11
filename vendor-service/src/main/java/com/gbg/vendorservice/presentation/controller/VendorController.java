package com.gbg.vendorservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.vendorservice.application.service.VendorService;
import com.gbg.vendorservice.infrastructure.config.auth.CustomUser;
import com.gbg.vendorservice.presentation.dto.request.CreateVendorRequestDto;
import com.gbg.vendorservice.presentation.dto.request.SearchVendorRequestDto;
import com.gbg.vendorservice.presentation.dto.request.UpdateVendorRequestDto;
import com.gbg.vendorservice.presentation.dto.response.CreateVendorResponseDto;
import com.gbg.vendorservice.presentation.dto.response.SearchVendorResponseDto;
import com.gbg.vendorservice.presentation.dto.response.VendorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;


    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    @Operation(summary = "업체 생성 API", description = "마스터, 허브관리자가 업체를 생성할 수 있습니다.")
    public ResponseEntity<BaseResponseDto<CreateVendorResponseDto>> createVendor(
        @AuthenticationPrincipal CustomUser user,
        @Valid @RequestBody CreateVendorRequestDto requestDto
    ) {
        CreateVendorResponseDto responseDto = vendorService.createVendor(user, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponseDto.success("업체 생성 성공", responseDto, HttpStatus.CREATED));
    }

    @GetMapping("/{vendorId}")
    @Operation(summary = "업체 단일 조회 API", description = "업체 ID로 단일 업체 정보를 조회합니다.")
    public ResponseEntity<BaseResponseDto<VendorResponseDto>> getVendor(
        @Parameter(description = "업체 UUID") @PathVariable UUID vendorId
    ) {
        VendorResponseDto responseDto = vendorService.getVendors(vendorId);
        return ResponseEntity.ok(
            BaseResponseDto.success("업체 단일 조회 성공", responseDto, HttpStatus.OK));
    }

    @GetMapping("/manager/{managerId}")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    @Operation(summary = "유저 ID로 업체 조회 API", description = "유저 ID를 통해 여러 업체를 조회합니다.")
    public ResponseEntity<BaseResponseDto<List<VendorResponseDto>>> getVendorsByManagerId(
        @Parameter(description = "유저 UUID") @PathVariable UUID managerId
    ) {
        List<VendorResponseDto> responseDto = vendorService.getVendorsByVendorManagerId(managerId);
        return ResponseEntity.ok(
            BaseResponseDto.success("유저 ID 기반 업체 조회 성공", responseDto, HttpStatus.OK)
        );
    }

    @GetMapping
    @Operation(summary = "전체 업체 목록 조회 API", description = "전체 업체 목록을 조회합니다.")
    public ResponseEntity<BaseResponseDto<List<VendorResponseDto>>> getAllVendors() {
        List<VendorResponseDto> responseDto = vendorService.getAllVendors();
        return ResponseEntity.ok(
            BaseResponseDto.success("업체 목록 조회 성공", responseDto, HttpStatus.OK));
    }

    @GetMapping("/search")
    @Operation(summary = "업체 검색 API", description = "업체명(name) 또는 유형(type: supplier/receiver)으로 검색합니다.")
    public ResponseEntity<BaseResponseDto<SearchVendorResponseDto>> searchVendors(
        @ModelAttribute SearchVendorRequestDto dto,
        Pageable pageable
    ) {
        SearchVendorResponseDto responseDto = vendorService.searchVendors(dto, pageable);
        return ResponseEntity.ok(
            BaseResponseDto.success("업체 검색 성공", responseDto, HttpStatus.OK));
    }


    @PutMapping("/{vendorId}")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    @Operation(summary = "업체 수정 API", description = "마스터, 허브관리자가 업체 정보를 수정할 수 있습니다.")
    public ResponseEntity<BaseResponseDto<VendorResponseDto>> updateVendor(
        @PathVariable UUID vendorId,
        @Valid @RequestBody UpdateVendorRequestDto requestDto
    ) {
        VendorResponseDto responseDto = vendorService.updateVendor(vendorId, requestDto);
        return ResponseEntity.ok(
            BaseResponseDto.success("업체 수정 성공", responseDto, HttpStatus.OK));
    }


    @DeleteMapping("/{vendorId}")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    @Operation(summary = "업체 삭제 API", description = "마스터, 허브관리자가 업체를 삭제할 수 있습니다.")
    public ResponseEntity<BaseResponseDto<Void>> deleteVendor(
        @PathVariable UUID vendorId
    ) {
        vendorService.deleteVendor(vendorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(BaseResponseDto.success("업체 삭제 성공", HttpStatus.NO_CONTENT));
    }
}
