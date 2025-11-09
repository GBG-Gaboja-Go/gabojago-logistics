package com.gbg.productservice.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InternalProductReleaseRequestDto {

    @Valid
    @NotNull
    private ProductDto product;

    @Getter
    @Builder
    public static class ProductDto {

        @NotNull(message = "상품 ID를 입력해주세요.")
        private UUID productId;

        @NotNull(message = "차감 수량을 입력해주세요.")
        private int quantity;

    }

}
