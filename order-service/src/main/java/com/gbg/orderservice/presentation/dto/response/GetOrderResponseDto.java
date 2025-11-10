package com.gbg.orderservice.presentation.dto.response;

import com.gbg.orderservice.domain.entity.Order;
import com.gbg.orderservice.domain.entity.enums.OrderStatus;
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

        private final UUID id;
        private final UUID producerVendorId;
        private final UUID receiverVendorId;
        private final UUID deliveryId;
        private final UUID productId;
        private final Integer quantity;
        private final BigInteger totalPrice;
        private final String requestMessage;
        private final OrderStatus status;

        public static GetOrderResponseDto.OrderDto from(Order order) {
            return OrderDto.builder()
                .id(order.getId())
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
