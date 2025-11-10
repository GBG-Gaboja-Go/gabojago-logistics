package com.gbg.orderservice.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
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

        @NotNull(message = "예상 거리를 입력하세요.(km)")
        @Min(value = 0, message = "거리는 0이상이어야 합니다.")
        private double estimatedDistance;

        @NotNull(message = "예상 소요시간을 입력하세요. (입력 \"HH:mm:ss\" 이렇게 해야합니다.)")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        private LocalTime estimatedTime;
    }

}
