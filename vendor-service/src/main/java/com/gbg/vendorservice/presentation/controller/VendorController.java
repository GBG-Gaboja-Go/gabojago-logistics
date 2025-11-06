package com.gbg.vendorservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gabojago.entity.BaseEntity;
import com.gbg.vendorservice.presentation.dto.request.CreateVendorRequestDto;
import com.gbg.vendorservice.presentation.dto.request.UpdateVendorRequestDto;
import com.gbg.vendorservice.presentation.dto.response.CreateVendorResponseDto;
import com.gbg.vendorservice.presentation.dto.response.VendorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/vendors")
public class VendorController extends BaseEntity {

    @PostMapping
    @Operation(summary = "업체 생성 API", description = "마스터, 허브관리자가 업체를 생성할 수 있습니다.")
    public ResponseEntity<BaseResponseDto<CreateVendorResponseDto>> createVendor(
        @Valid @RequestBody CreateVendorRequestDto requestDto
    ) {
        UUID newVendorId = UUID.randomUUID();

        CreateVendorResponseDto responseDto = CreateVendorResponseDto.builder()
            .id(newVendorId)
            .build();

        return ResponseEntity.ok(
            BaseResponseDto.success("업체 생성 성공", responseDto, HttpStatus.CREATED));
    }

    @GetMapping("/{vendorId}")
    @Operation(summary = "업체 단일 조회 API", description = "업체 ID로 단일 업체 정보를 조회합니다.")
    public ResponseEntity<BaseResponseDto<VendorResponseDto>> getVendor(
        @Parameter(description = "업체 UUID") @PathVariable UUID vendorId
    ) {
        VendorResponseDto responseDto = VendorResponseDto.builder()
            .id(vendorId)
            .name("가보자고 로지스틱스")
            .hubId(UUID.randomUUID())
            .managerId(UUID.randomUUID())
            .address("서울시 강남구 물류로 123")
            .supplier(true)
            .receiver(false)
            .createdBy("system_admin")
            .createdAt(LocalDateTime.now().minusDays(3))
            .updatedBy("hub_manager")
            .updatedAt(LocalDateTime.now())
            .deleted(false)
            .build();

        return ResponseEntity.ok(
            BaseResponseDto.success("업체 단일 조회 성공", responseDto, HttpStatus.OK));
    }

    @GetMapping
    @Operation(summary = "업체 목록 조회 API", description = "전체 업체 목록을 조회합니다. (name, type 파라미터로 필터링 가능)")
    public ResponseEntity<BaseResponseDto<List<VendorResponseDto>>> getVendors(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String type
    ) {
        List<VendorResponseDto> vendors = IntStream.range(1, 6)
            .mapToObj(i -> VendorResponseDto.builder()
                .id(UUID.randomUUID())
                .name("가보자고 물류센터 " + i)
                .hubId(UUID.randomUUID())
                .managerId(UUID.randomUUID())
                .address("서울시 송파구 물류로 " + i + "번지")
                .supplier(i % 2 == 0)
                .receiver(i % 2 != 0)
                .createdBy("hub_manager_" + i)
                .createdAt(LocalDateTime.now().minusDays(i))
                .deleted(false)
                .build())
            .collect(Collectors.toList());

        return ResponseEntity.ok(BaseResponseDto.success("업체 목록 조회 성공", vendors, HttpStatus.OK));
    }

    @GetMapping("/search")
    @Operation(summary = "업체 검색 API", description = "업체명(name) 또는 유형(type: supplier/receiver)으로 검색합니다.")
    public ResponseEntity<BaseResponseDto<List<VendorResponseDto>>> searchVendors(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String type
    ) {
        List<VendorResponseDto> vendors = IntStream.range(1, 6)
            .mapToObj(i -> VendorResponseDto.builder()
                .id(UUID.randomUUID())
                .name("가보자고 물류센터 " + i)
                .hubId(UUID.randomUUID())
                .managerId(UUID.randomUUID())
                .address("서울시 송파구 물류로 " + i + "번지")
                .supplier(i % 2 == 0)
                .receiver(i % 2 != 0)
                .createdBy("hub_manager_" + i)
                .createdAt(LocalDateTime.now().minusDays(i))
                .deleted(false)
                .build())
            // 더미데이터 기반 필터링
            .filter(v -> name == null || v.getName().contains(name))
            .filter(v -> type == null ||
                (type.equalsIgnoreCase("supplier") && v.isSupplier()) ||
                (type.equalsIgnoreCase("receiver") && v.isReceiver()))
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            BaseResponseDto.success("업체 검색 성공", vendors, HttpStatus.OK));
    }

    @PutMapping("/{vendorId}")
    @Operation(summary = "업체 수정 API", description = "업체 정보를 수정합니다.")
    public ResponseEntity<BaseResponseDto<VendorResponseDto>> updateVendor(
        @Parameter(description = "업체 UUID") @PathVariable UUID vendorId,
        @Valid @RequestBody UpdateVendorRequestDto requestDto
    ) {
        VendorResponseDto responseDto = VendorResponseDto.builder()
            .id(vendorId)
            .name(requestDto.getName() != null ? requestDto.getName() : "기존 업체명")
            .hubId(requestDto.getHubId() != null ? requestDto.getHubId() : UUID.randomUUID())
            .managerId(
                requestDto.getManagerId() != null ? requestDto.getManagerId() : UUID.randomUUID())
            .address(requestDto.getAddress() != null ? requestDto.getAddress() : "기존 주소")
            .supplier(requestDto.getIsSupplier() != null && requestDto.getIsSupplier())
            .receiver(requestDto.getIsReceiver() != null && requestDto.getIsReceiver())
            .updatedBy("master_admin")
            .updatedAt(LocalDateTime.now())
            .build();

        return ResponseEntity.ok(
            BaseResponseDto.success("업체 수정 성공", responseDto, HttpStatus.OK));
    }

    @DeleteMapping("/{vendorId}")
    @Operation(summary = "업체 삭제 API", description = "업체를 삭제합니다. (Soft Delete)")
    public ResponseEntity<BaseResponseDto<Void>> deleteVendor(
        @Parameter(description = "업체 UUID") @PathVariable UUID vendorId
    ) {
        return ResponseEntity.ok(BaseResponseDto.success("업체 삭제 성공", HttpStatus.NO_CONTENT));
    }
}
