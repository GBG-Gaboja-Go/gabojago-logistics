package com.gbg.deliveryservice.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 내부 AI 연동용 요청 DTO - Slack 알림 메시지 및 최종 발송시한 계산용
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternalCreateAIRequestDto {

    @Valid
    @NotNull(message = "ai에 입력할 정보를 입력해주세요.")
    private AIDto ai;

    @Getter
    @Builder
    public static class AIDto {

        /**
         * 주문 정보
         */
        @NotNull(message = "주문 UUID를 입력해주세요.")
        private UUID orderId;

        @NotBlank(message = "주문자 이름을 입력해주세요.")
        private String ordererName;

        @Email(message = "주문자 이메일 형식이 올바르지 않습니다.")
        private String ordererEmail;

        @NotNull(message = "주문 시간을 입력해주세요.")
        private LocalDateTime orderTime;

        private String orderRequestMessage;

        /**
         * 상품 정보
         */
        @NotNull(message = "상품 이름을 입력해주세요.")
        private final String productName;

        @NotNull(message = "상품 수량을 입력해주세요.")
        @Positive(message = "상품 수량은 양수여야 합니다.")
        private final Integer productQuantity;


        /**
         * 배송 경로 정보
         */
        @NotNull(message = "발송지 정보를 입력해주세요.")
        private Location pickup;

        private List<Location> via; // 경유지 리스트 (nullable)

        @NotNull(message = "도착지 정보를 입력해주세요.")
        private Location destination;

        /**
         * 배송 담당자
         */
        @NotBlank(message = "배송 담당자 이름을 입력해주세요.")
        private String deliveryPersonName;

        @Email(message = "배송 담당자 이메일 형식이 올바르지 않습니다.")
        private String deliveryPersonEmail;

        /**
         * 배송 담당자 근무시간 (null 가능, 기본 09~18시)
         */
        private WorkTime driverWorkTime;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Location {

            @NotBlank
            private String name; // 예: '서울 허브 A'

            private String address;

            private Double lat;
            private Double lon;

        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class WorkTime {

            @Min(0)
            @Max(23)
            @Builder.Default
            private int startHour = 9; // 예: 9

            @Min(0)
            @Max(23)
            @Builder.Default
            private int endHour = 18;   // 예: 18
        }
    }

}
