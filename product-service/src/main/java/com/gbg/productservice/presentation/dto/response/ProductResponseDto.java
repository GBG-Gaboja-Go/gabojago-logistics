package com.gbg.productservice.presentation.dto.response;

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
public class ProductResponseDto {

    private ProductDto product;


    @Getter
    @Builder
    public static class ProductDto {

        private final UUID id;
        private final String name;
        private final UUID vendorId;
        private final Integer stock;
        private final BigInteger price;
    }
}
