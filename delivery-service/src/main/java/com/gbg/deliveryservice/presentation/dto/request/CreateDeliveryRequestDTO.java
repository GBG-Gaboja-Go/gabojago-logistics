package com.gbg.deliveryservice.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

public record CreateDeliveryRequestDTO(
    @Valid
    @NotNull(message = "배송 정보를 입력해주세요")
    DeliveryDTO delivery
) {

    @Getter
    @Builder
    public static class DeliveryDTO {

        @NotNull(message = "주문 ID를 입력해주세요.")
        private UUID orderId;

        @NotNull(message = "배송지 주소를 입력해주세요.")
        private String deliveryAddress;

        @NotNull(message = "출발 허브 아이디를 입력해주세요")
        private UUID hubFromId;

        @NotNull(message = "도착 허브 아이디를 입력해주세요")
        private UUID hubToId;

        @NotNull(message = "공급 업체 아이디를 입력해주세요")
        private UUID userFromId;

        @NotNull(message = "수령 업체 아이디(주문자)를 입력해주세요")
        private UUID userToId;

    }

}
