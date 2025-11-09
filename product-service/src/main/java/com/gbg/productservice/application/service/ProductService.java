package com.gbg.productservice.application.service;

import com.gbg.productservice.presentation.dto.request.InternalProductReleaseRequestDto;

public interface ProductService {

    void postInternalProductsReleaseStock(InternalProductReleaseRequestDto requestDto);
}
