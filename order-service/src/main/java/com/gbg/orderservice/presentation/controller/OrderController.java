package com.gbg.orderservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gabojago.dto.PageResponseDto;
import com.gbg.orderservice.application.service.OrderService;
import com.gbg.orderservice.infrastructure.config.auth.CustomUser;
import com.gbg.orderservice.presentation.dto.request.CreateOrderRequestDto;
import com.gbg.orderservice.presentation.dto.request.OrderSearchRequestDto;
import com.gbg.orderservice.presentation.dto.response.CreateOrderResponseDto;
import com.gbg.orderservice.presentation.dto.response.GetOrderResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "주문 API", description = "주문 API 입니다.")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('VENDOR_MANAGER')")
    @Operation(summary = "주문 생성 API", description = "수령업체만 주문을 생성할 수 있습니다.")
    public ResponseEntity<BaseResponseDto<CreateOrderResponseDto>> createOrder(
        @AuthenticationPrincipal CustomUser customUser,
        @Valid @RequestBody CreateOrderRequestDto requestDto
    ) {
        CreateOrderResponseDto responseDto = orderService.createOrder(customUser, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponseDto.success("주문 생성 성공", responseDto, HttpStatus.CREATED));
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('MASTER', 'VENDOR_MANAGER', 'HUB_MANAGER')")
    @Operation(summary = "주문 전체 조회 API", description = "수령업체는 본인 주문을 모두 조회할 수 있습니다.(마스터는 모든 수령업체 주문 조회 가능)")
    public ResponseEntity<BaseResponseDto<PageResponseDto<GetOrderResponseDto>>> getOrders(
        @RequestBody(required = false) OrderSearchRequestDto searchRequestDto,
        Pageable pageable
    ) {
        Page<GetOrderResponseDto> result = orderService.searchOrders(searchRequestDto, pageable);
        return ResponseEntity.ok(
            BaseResponseDto.success("메뉴 목록 조회 성공", PageResponseDto.from(result), HttpStatus.OK));
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('MASTER', 'VENDOR_MANAGER', 'HUB_MANAGER')")
    @Operation(summary = "상세 주문 조회 API", description = "상세 주문을 조회합니다.")
    public ResponseEntity<BaseResponseDto<GetOrderResponseDto>> getOrder(
        @Parameter(description = "order UUID") @PathVariable UUID orderId
    ) {
        GetOrderResponseDto responseDto = orderService.getOrder(orderId);
        return ResponseEntity.ok(
            BaseResponseDto.success("상세 주문 조회 성공", responseDto, HttpStatus.OK));
    }

    @PostMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('MASTER', 'VENDOR_MANAGER')")
    @Operation(summary = "주문 취소 API", description = "상품 담당 허브 관리자와 공급업체는 주문을 취소할 수 있습니다.")
    public ResponseEntity<BaseResponseDto<Void>> postOrderCancel(
        @Parameter(description = "order UUID") @PathVariable UUID orderId
    ) {
        orderService.postOrderCancel(orderId);
        return ResponseEntity.ok(BaseResponseDto.success("주문 취소 성공", HttpStatus.NO_CONTENT));
    }
}
