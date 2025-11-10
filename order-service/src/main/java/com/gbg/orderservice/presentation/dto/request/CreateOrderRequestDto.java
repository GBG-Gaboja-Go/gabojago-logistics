package com.gbg.orderservice.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {

    @Valid
    @NotNull(message = "주문 정보를 입력해주세요.")
    private OrderDto order;

    @Getter
    @Builder
    public static class OrderDto {

        @NotNull(message = "주문 상품을 입력해주세요.")
        @Schema(description = "상품 UUID", example = "a1b2c3d4-e5f6-7890-abcd-123456789012")
        private UUID productId;

        @NotNull(message = "주문 상품 수량을 입력해주세요.")
        @Min(value = 1, message = "주문 상품을 1개 이상 입력해주세요.")
        @Schema(description = "상품 구매 수량", example = "1")
        private Integer quantity;

        @Schema(description = "주문 요청사항", example = "내일 오후 6시 전에 배송해주세요.")
        private String requestMessage;
    }
}
