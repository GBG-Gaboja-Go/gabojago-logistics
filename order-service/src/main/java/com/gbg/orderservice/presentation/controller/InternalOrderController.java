package com.gbg.orderservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.orderservice.application.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/v1/orders")
@RequiredArgsConstructor
@Tag(name = "주문 내부 API", description = "주문 내부 API 입니다.")
public class InternalOrderController {

    private final OrderService orderService;

    @PostMapping("/{orderId}/deliverying")
    @Operation(summary = "주문 DELIVERYING 상태 변경 API", description = "배송이 시작되었을 때 내부적으로 상태 변경을 요청할 수 있습니다.")
    public ResponseEntity<BaseResponseDto<Void>> postInternalOrderDelivering(
        @Parameter(description = "order UUID") @PathVariable UUID orderId
    ) {
        orderService.postInternalOrderDelivering(orderId);
        return ResponseEntity.ok(
            BaseResponseDto.success("주문 상태 DELIVERING 변경 성공", HttpStatus.NO_CONTENT));
    }

    @PostMapping("/{orderId}/delivered")
    @Operation(summary = "주문 DELIVERED 상태 변경 API", description = "수령업체한테 배송완료하면 내부적으로 상태 변경을 요청할 수 있습니다.")
    public ResponseEntity<BaseResponseDto<Void>> postInternalOrderDelivered(
        @Parameter(description = "order UUID") @PathVariable UUID orderId
    ) {
        orderService.postInternalOrderDelivered(orderId);
        return ResponseEntity.ok(
            BaseResponseDto.success("주문 상태 DELIVERYING로 변경 성공", HttpStatus.NO_CONTENT));
    }
}
