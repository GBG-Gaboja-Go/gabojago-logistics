package com.gbg.productservice.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequestDto {

    @Valid
    @NotNull(message = "상품 정보를 입력해주세요.")
    private ProductDto product;

    @Getter
    @Builder
    public static class ProductDto {

        @NotNull(message = "상품명을 입력해주세요.")
        private String name;

        private UUID vendorId;
        
        private UUID hubId;

        @NotNull(message = "재고 수량을 입력해주세요.")
        @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
        private Integer stock;

        @NotNull(message = "가격을 입력해주세요.")
        private BigInteger price;
    }
}
