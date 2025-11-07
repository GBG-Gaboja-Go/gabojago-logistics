package com.gbg.deliveryservice.presentation.dto.request;

import com.gbg.deliveryservice.domain.entity.enums.DeliveryStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public record UpdateDeliveryStatusRequestDTO(

    @Valid
    @NotNull(message = "배송 정보를 입력해주세요.")
    DeliveryStatusDTO delivery

) {

    @Getter
    @Builder
    public static class DeliveryStatusDTO {

        @NotNull(message = "상태를 입력해주세요.")
        private DeliveryStatus status;

    }

}
