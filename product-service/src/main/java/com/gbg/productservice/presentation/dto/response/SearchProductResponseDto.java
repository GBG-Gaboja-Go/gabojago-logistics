package com.gbg.productservice.presentation.dto.response;

import com.gbg.productservice.domain.entity.Product;
import com.gbg.productservice.presentation.dto.response.ProductResponseDto.ProductDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductResponseDto {

    private List<ProductDto> products;
    private long totalElements;
    private int totalPages;
    private int currentPage;

    public static SearchProductResponseDto from(Page<Product> productPage) {
        List<ProductResponseDto.ProductDto> productDtos = productPage.getContent().stream()
            .map(product -> ProductResponseDto.ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .vendorId(product.getVendorId())
                .stock(product.getStock())
                .price(product.getPrice())
                .build())
            .collect(Collectors.toList());

        return SearchProductResponseDto.builder()
            .products(productDtos)
            .totalElements(productPage.getTotalElements())
            .totalPages(productPage.getTotalPages())
            .currentPage(productPage.getNumber())
            .build();
    }
}
