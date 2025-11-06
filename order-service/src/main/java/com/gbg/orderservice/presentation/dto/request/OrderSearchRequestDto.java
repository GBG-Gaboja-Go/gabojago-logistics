package com.gbg.orderservice.presentation.dto.request;

import com.gbg.orderservice.domain.entity.enums.OrderStatus;
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

        private UUID producerVendorId;
        private UUID receiverVendorId;
        private OrderStatus status;
        private LocalDate dateFrom;
        private LocalDate dateTo;
    }

}
