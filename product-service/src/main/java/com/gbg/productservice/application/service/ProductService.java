package com.gbg.productservice.application.service;

import com.gbg.productservice.presentation.dto.request.InternalProductReleaseRequestDto;
import com.gbg.productservice.presentation.dto.request.InternalProductReturnRequestDto;

public interface ProductService {

    void postInternalProductReleaseStock(InternalProductReleaseRequestDto requestDto);

    void postInternalProductReturnStock(InternalProductReturnRequestDto requestDto);
}
