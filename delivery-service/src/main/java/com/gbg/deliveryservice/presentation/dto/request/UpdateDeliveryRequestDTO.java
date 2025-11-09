package com.gbg.deliveryservice.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

public record UpdateDeliveryRequestDTO(
    @Valid
    @NotNull(message = "배송 정보를 입력해주세요")
    DeliveryDTO delivery
) {

    @Getter
    @Builder
    public static class DeliveryDTO {

        @NotNull(message = "배송지 주소를 입력해주세요.")
        private String deliveryAddress;

        @NotNull(message = "예상 거리를 입력하세요.(km)")
        @Min(value = 0, message = "거리는 0이상이어야 합니다.")
        private double estimatedDistance;

        @NotNull(message = "예상 소요시간을 입력하세요. (입력 \"HH:mm:ss\" 이렇게 해야합니다.)")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        private LocalTime estimatedTime;

        private boolean isUpdateStartTime;

        private LocalDateTime startedTime;

        private boolean isUpdateCompletedTime;

        private LocalDateTime completedTime;
    }

}
