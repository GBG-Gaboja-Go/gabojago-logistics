package com.gbg.productservice.application.service;

import com.gbg.productservice.presentation.dto.request.CreateProductRequestDto;
import com.gbg.productservice.presentation.dto.request.InternalProductReleaseRequestDto;
import com.gbg.productservice.presentation.dto.request.InternalProductReturnRequestDto;
import com.gbg.productservice.presentation.dto.request.SearchProductRequestDto;
import com.gbg.productservice.presentation.dto.request.UpdateProductRequestDto;
import com.gbg.productservice.presentation.dto.response.CreateProductResponseDto;
import com.gbg.productservice.presentation.dto.response.ProductResponseDto;
import com.gbg.productservice.presentation.dto.response.SearchProductResponseDto;
import com.gbg.productservice.presentation.dto.response.UpdateProductResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    CreateProductResponseDto createProduct(CreateProductRequestDto dto);

    ProductResponseDto getProductById(UUID productId);

    List<ProductResponseDto> getProductsByVendor(UUID vendorId);

    SearchProductResponseDto searchProducts(SearchProductRequestDto dto, Pageable pageable);


    UpdateProductResponseDto updateProduct(UUID id, UpdateProductRequestDto dto);

    void deleteProduct(UUID id);

    void postInternalProductReleaseStock(InternalProductReleaseRequestDto dto);

    void postInternalProductReturnStock(InternalProductReturnRequestDto dto);
}
