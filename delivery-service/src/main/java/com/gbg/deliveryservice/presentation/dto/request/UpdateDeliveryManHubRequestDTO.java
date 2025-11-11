package com.gbg.deliveryservice.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

public record UpdateDeliveryManHubRequestDTO(

    @Valid
    @NotNull(message = "배송 담당자 정보를 입력해주세요.")
    DeliveryManDTO deliveryMan

) {

    @Getter
    @Builder
    public static class DeliveryManDTO {

        @NotNull(message = "변경할 허브를 입력해주세요.")
        private UUID hubId;

    }

}
