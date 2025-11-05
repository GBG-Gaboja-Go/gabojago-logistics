package com.gbg.deliveryservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.deliveryservice.domain.enums.DeliveryStatus;
import com.gbg.deliveryservice.presentation.dto.request.CreateDeliveryRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryStatusRequestDTO;
import com.gbg.deliveryservice.presentation.dto.response.CreateDeliveryResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryPageResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetMyDeliveryResponseDTO;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Any;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/delivery")
public class DeliveryController {

    @PostMapping
    public ResponseEntity<BaseResponseDto<CreateDeliveryResponseDTO>> createDelivery(
        @RequestBody CreateDeliveryRequestDTO req
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponseDto.success("배달이 생성되었습니다.",
                CreateDeliveryResponseDTO.from(UUID.randomUUID())));
    }

    @GetMapping
    public ResponseEntity<BaseResponseDto<GetDeliveryPageResponseDTO>> getDeliveryPage(
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable,
        @RequestParam(required = false) DeliveryStatus status,
        @RequestParam(required = false) UUID sender,
        @RequestParam(required = false) UUID receiver,
        @RequestParam(required = false) UUID product
    ) {

        List<GetDeliveryPageResponseDTO.DeliveryDto> deliveries = List.of(
            GetDeliveryPageResponseDTO.DeliveryDto.builder()
                .id(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .deliveryAddress("서울특별시 종로구 세종대로 172")
                .estimatedDistance(12.2)
                .estimatedTime(LocalTime.of(1, 23, 13))
                .status(DeliveryStatus.OUT_FOR_DELIVERY)
                .createdAt(LocalDateTime.now())
                .build(),

            GetDeliveryPageResponseDTO.DeliveryDto.builder()
                .id(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .deliveryAddress("서울특별시 강남구 강남대로 172")
                .estimatedDistance(7.8)
                .estimatedTime(LocalTime.of(0, 45, 12))
                .status(DeliveryStatus.OUT_FOR_DELIVERY)
                .createdAt(LocalDateTime.now())
                .build(),

            GetDeliveryPageResponseDTO.DeliveryDto.builder()
                .id(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .deliveryAddress("경기도 가평군 화악산로 35")
                .estimatedDistance(21.5)
                .estimatedTime(LocalTime.of(2, 10, 5))
                .status(DeliveryStatus.DELIVERED)
                .createdAt(LocalDateTime.now())
                .build()
        );

        GetDeliveryPageResponseDTO.PageInfo pageInfo = GetDeliveryPageResponseDTO.PageInfo.from(
            pageable.getPageNumber(), pageable.getPageSize(), 5, 10L);

        return ResponseEntity.ok(
            BaseResponseDto.success("배달 목록입니다.",
                GetDeliveryPageResponseDTO.from(deliveries, pageInfo)));
    }


    @GetMapping("/{deliveryId}")
    public ResponseEntity<BaseResponseDto<GetDeliveryResponseDTO>> getDelivery(
        @PathVariable("deliveryId") UUID id
    ) {

        GetDeliveryResponseDTO.DeliveryDTO res = GetDeliveryResponseDTO.DeliveryDTO.builder()
            .id(UUID.randomUUID())
            .orderId(UUID.randomUUID())
            .deliveryAddress("경기도 가평군 화악산로 35")
            .estimatedDistance(21.5)
            .estimatedTime(LocalTime.of(2, 10, 5))
            .status(DeliveryStatus.DELIVERED)
            .createdAt(LocalDateTime.now())
            .build();

        return ResponseEntity.ok(BaseResponseDto.success("", GetDeliveryResponseDTO.from(res)));
    }

    @PutMapping("/{deliveryId}")
    public ResponseEntity<BaseResponseDto<Any>> updateDelivery(
        @PathVariable("deliveryId") UUID id,
        @RequestBody UpdateDeliveryRequestDTO req
    ) {

        return ResponseEntity.ok(BaseResponseDto.success("배달정보가 변경되었습니다.", null));
    }

    @PatchMapping("/{deliveryId}/status")
    public ResponseEntity<BaseResponseDto<Any>> updateDeliveryStatus(
        @PathVariable("deliveryId") UUID id,
        @RequestBody UpdateDeliveryStatusRequestDTO req
    ) {

        return ResponseEntity.ok(BaseResponseDto.success("상태가 변경되었습니다.", null));
    }

    @GetMapping("/my")
    public ResponseEntity<BaseResponseDto<GetMyDeliveryResponseDTO>> getMyDeliveryPage(
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable,
        @RequestParam(required = false) DeliveryStatus status,
        @RequestParam(required = false) UUID sender,
        @RequestParam(required = false) UUID receiver,
        @RequestParam(required = false) UUID product
    ) {
        List<GetMyDeliveryResponseDTO.DeliveryDTO> deliveries = List.of(
            GetMyDeliveryResponseDTO.DeliveryDTO.builder()
                .id(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .deliveryAddress("서울특별시 종로구 세종대로 172")
                .estimatedDistance(12.2)
                .estimatedTime(LocalTime.of(1, 23, 13))
                .status(DeliveryStatus.OUT_FOR_DELIVERY)
                .createdAt(LocalDateTime.now())
                .build(),

            GetMyDeliveryResponseDTO.DeliveryDTO.builder()
                .id(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .deliveryAddress("서울특별시 강남구 강남대로 172")
                .estimatedDistance(7.8)
                .estimatedTime(LocalTime.of(0, 45, 12))
                .status(DeliveryStatus.OUT_FOR_DELIVERY)
                .createdAt(LocalDateTime.now())
                .build(),

            GetMyDeliveryResponseDTO.DeliveryDTO.builder()
                .id(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .deliveryAddress("경기도 가평군 화악산로 35")
                .estimatedDistance(21.5)
                .estimatedTime(LocalTime.of(2, 10, 5))
                .status(DeliveryStatus.DELIVERED)
                .createdAt(LocalDateTime.now())
                .build()
        );
        GetMyDeliveryResponseDTO.PageInfo pageInfo = GetMyDeliveryResponseDTO.PageInfo.from(
            pageable.getPageNumber(), pageable.getPageSize(), 5, 10L);

        return ResponseEntity.ok(
            BaseResponseDto.success("배달담당자 본인의 배달 목록입니다.",
                GetMyDeliveryResponseDTO.from(deliveries, pageInfo)));
    }
}



