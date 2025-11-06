package com.gbg.orderservice.presentation.dto.request;

import com.gbg.orderservice.domain.entity.enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequestDto {

    @Valid
    @NotNull(message = "주문 정보를 입력해주세요.")
    private OrderDto order;

    @Getter
    @Builder
    public static class OrderDto {

        @NotNull(message = "주문 상태를 입력해주세요.")
        private OrderStatus status;
    }
}
