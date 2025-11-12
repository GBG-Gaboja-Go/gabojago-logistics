package com.gbg.productservice.presentation.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductResponseDto {

    private ProductDto product;

    @Getter
    @Builder
    public static class ProductDto {

        private UUID id;
    }

    public static CreateProductResponseDto from(UUID id) {
        return CreateProductResponseDto.builder()
            .product(ProductDto.builder()
                .id(id)
                .build())
            .build();
    }
}
