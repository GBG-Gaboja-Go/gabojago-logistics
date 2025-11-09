package com.gbg.productservice.application.service.impl;

import com.gbg.productservice.application.service.ProductService;
import com.gbg.productservice.presentation.dto.request.InternalProductReleaseRequestDto;
import com.gbg.productservice.presentation.dto.request.InternalProductReturnRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Override
    @Transactional
    public void postInternalProductReleaseStock(InternalProductReleaseRequestDto requestDto) {
        // prouctId로 해당 product 찾아서
        // 해당 수량만큼 감소한다.
        log.info("Product id: {} quantity: {} 만큼 감소됐어요. -> 재고 감소 완료",
            requestDto.getProduct().getProductId(),
            requestDto.getProduct().getQuantity());
    }

    @Override
    public void postInternalProductReturnStock(InternalProductReturnRequestDto requestDto) {
        // prouctId로 해당 product 찾아서
        // 해당 수량만큼 증가한다.
        log.info("Product id: {} quantity: {} 만큼 증가됐어요. -> 재고 복원 완료",
            requestDto.getProduct().getProductId(),
            requestDto.getProduct().getQuantity());
    }
}
