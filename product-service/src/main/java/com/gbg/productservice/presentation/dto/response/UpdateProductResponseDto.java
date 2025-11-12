package com.gbg.productservice.presentation.dto.response;

import com.gbg.productservice.domain.entity.Product;
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
public class UpdateProductResponseDto {

    private ProductDto product;

    @Getter
    @Builder
    public static class ProductDto {

        private UUID id;
        private String name;
        private UUID vendorId;
        private Integer stock;
        private BigInteger price;
    }

    public static UpdateProductResponseDto from(
        Product product) {
        return UpdateProductResponseDto.builder()
            .product(ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .vendorId(product.getVendorId())
                .stock(product.getStock())
                .price(product.getPrice())
                .build())
            .build();
    }
}
