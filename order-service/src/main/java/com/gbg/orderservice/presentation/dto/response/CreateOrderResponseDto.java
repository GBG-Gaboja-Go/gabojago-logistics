package com.gbg.orderservice.presentation.dto.response;

import com.gbg.orderservice.domain.entity.Order;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderResponseDto {

    private final OrderDto order;

    @Getter
    @Builder
    public static class OrderDto {

        private final UUID id;

        public static OrderDto from(Order order) {
            return OrderDto.builder()
                .id(order.getId())
                .build();
        }
    }

    public static CreateOrderResponseDto from(Order order) {
        return CreateOrderResponseDto.builder()
            .order(OrderDto.from(order))
            .build();
    }
}
