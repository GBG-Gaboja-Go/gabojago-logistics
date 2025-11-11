package com.gbg.orderservice.presentation.dto.response;

import com.gbg.orderservice.domain.entity.Order;
import com.gbg.orderservice.domain.entity.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigInteger;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetOrderResponseDto {

    private final OrderDto order;

    @Getter
    @Builder
    public static class OrderDto {

        @Schema(description = "주문 ID", example = "1a2b3c4d-5e6f-7890-abcd-123456789012")
        private final UUID id;

        @Schema(description = "주문자 ID", example = "06432270-2fb0-4a94-9e6c-effdb399d6c3")
        private final UUID userId;

        @Schema(description = "생산자 업체 ID", example = "7f6e5d4c-3b2a-1908-7654-3210fedcba98")
        private final UUID producerVendorId;

        @Schema(description = "수신자 업체 ID", example = "abcd1234-ef56-7890-1234-56789abcdef0")
        private final UUID receiverVendorId;

        @Schema(description = "배송 ID", example = "9b8a7c6d-5e4f-3a2b-1c0d-ef1234567890")
        private final UUID deliveryId;

        @Schema(description = "상품 UUID", example = "a1b2c3d4-e5f6-7890-abcd-123456789012")
        private final UUID productId;

        @Schema(description = "상품 수량", example = "3")
        private final Integer quantity;

        @Schema(description = "총 가격 (단위: 원)", example = "45000")
        private final BigInteger totalPrice;

        @Schema(description = "주문 요청사항", example = "포장 신경써서 부탁드립니다.")
        private final String requestMessage;

        @Schema(description = "주문 상태", example = "PENDING")
        private final OrderStatus status;

        public static GetOrderResponseDto.OrderDto from(Order order) {
            return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .producerVendorId(order.getProducerVendorId())
                .receiverVendorId(order.getReceiverVendorId())
                .deliveryId(order.getDeliveryId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .requestMessage(order.getRequestMessage())
                .status(order.getStatus())
                .build();
        }
    }

    public static GetOrderResponseDto from(Order order) {
        return GetOrderResponseDto.builder()
            .order(GetOrderResponseDto.OrderDto.from(order))
            .build();
    }


}
