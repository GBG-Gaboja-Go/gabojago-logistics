package com.gbg.productservice.application.service.impl;

import com.gbg.productservice.application.service.ProductService;
import com.gbg.productservice.presentation.dto.request.InternalProductReleaseRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Override
    @Transactional
    public void postInternalProductsReleaseStock(InternalProductReleaseRequestDto requestDto) {
        // prouctId로 해당 product 찾아서
        // 해당 수량 감소한다.
        log.info("Product id: {} quantity: {} 만큼 감소됐어요.", requestDto.getProduct().getProductId(),
            requestDto.getProduct().getQuantity());
    }
}
