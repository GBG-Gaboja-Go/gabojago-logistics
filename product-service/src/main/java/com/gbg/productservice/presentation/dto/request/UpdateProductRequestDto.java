package com.gbg.productservice.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequestDto {

    @Valid
    @NotNull(message = "상품 정보를 입력해주세요.")
    private ProductDto product;

    @Getter
    @Builder
    public static class ProductDto {

        private String name;
        private UUID vendorId;
        private Integer stock;
        private BigInteger price;

    }

}
