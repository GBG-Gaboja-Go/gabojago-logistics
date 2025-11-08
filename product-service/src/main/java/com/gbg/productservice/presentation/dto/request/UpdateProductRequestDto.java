package com.gbg.productservice.presentation.dto.request;

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

    private String name;
    private UUID vendorId;
    private Integer stock;
    private Integer price;
    private Integer minOrderQty;
}
