package com.gbg.deliveryservice.presentation.dto.request;

import com.gbg.deliveryservice.domain.entity.enums.DeliveryType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

public record UpdateDeliveryManRequestDTO(
    @Valid
    @NotNull(message = "배송 담당자 정보를 입력해주세요")
    DeliveryManDTO delivery
) {

    @Getter
    @Builder
    public static class DeliveryManDTO {

        @NotNull(message = "소속 허브 ID를 입력해주세요.")
        private UUID hubId;

        @NotNull(message = "userId를 입력해주세요")
        private UUID userId;

        @NotNull(message = "배송 담당자 타입을 입력해주세요.")
        private DeliveryType type;

        @Min(value = 1, message = "배송 순번은 1에서 10까지입니다.")
        @Max(value = 10, message = "배송 순번을 입력하세요.")
        private int sequence;

    }

}
