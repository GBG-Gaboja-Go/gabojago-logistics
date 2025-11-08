package com.gbg.productservice.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "상품명을 입력해주세요.")
    private String name;

    @NotNull(message = "업체 ID를 입력해주세요.")
    private UUID vendorId;

    @NotNull(message = "재고 수량을 입력해주세요.")
    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    private Integer stock;

    @NotNull(message = "가격을 입력해주세요.")
    private Integer price;
}
