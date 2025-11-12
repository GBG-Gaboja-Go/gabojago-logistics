package com.gbg.deliveryservice.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

public record CreateDeliveryManRequestDTO(
    @Valid
    @NotNull(message = "배송 담당자 정보를 입력해주세요")
    DeliveryManDTO deliveryman
) {

    @Getter
    @Builder
    public static class DeliveryManDTO {

        private UUID hubId;

        @NotNull(message = "userId를 입력해주세요")
        private UUID userId;


        @Min(value = -1, message = "배송 순번은 1에서 10까지입니다. (-1 은 근무 할 수 없는 배달 매니저)")
        @Max(value = 10, message = "배송 순번을 입력하세요.")
        private int sequence;

    }

}
