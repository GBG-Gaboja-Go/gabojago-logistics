package com.gbg.orderservice.presentation.dto.request;

import com.gbg.orderservice.domain.entity.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchRequestDto {

    @Valid
    @NotNull(message = "검색할 주문 정보를 입력해주세요.")
    private OrderDto order;

    @Getter
    @Builder
    public static class OrderDto {

        @Schema(description = "주문한 사용자 UUID", example = "c1fbd6a0-1f4d-4b9d-8f53-0c7d7b3f8e3a")
        private UUID userId;

        @Schema(description = "공급 업체 담당 허브 UUID", example = "ac3b722b-a5c0-477f-a180-288e92481067")
        private UUID hubId;

        @Schema(description = "공급 업체 UUID", example = "7a3d48b4-6d2a-4b89-8bfa-91c9d4b9e123")
        private UUID producerVendorId;

        @Schema(description = "수령 업체 UUID", example = "b72b6e6b-142a-4d93-bb27-5b2c9a4a3b12")
        private UUID receiverVendorId;

        @Schema(description = "상품 UUID", example = "9bcd1d21-24c5-4db9-97e1-02e1b6d89a98")
        private UUID productId;

        @Schema(description = "주문 상태", example = "CREATED")
        private OrderStatus status;

        @Schema(description = "조회 시작일", example = "2025-11-01")
        private LocalDate dateFrom;

        @Schema(description = "조회 종료일", example = "2025-11-10")
        private LocalDate dateTo;
    }

}
