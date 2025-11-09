package com.gbg.productservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.productservice.application.service.ProductService;
import com.gbg.productservice.presentation.dto.request.InternalProductReleaseRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/v1/products")
@AllArgsConstructor
public class InternalProductController {

    private final ProductService productService;

    @PostMapping("/release-stock")
    @Operation(summary = "상품 재고 감소 내부 API", description = "주문이 생성되면 해당 상품 재고를 감소합니다.")
    public ResponseEntity<BaseResponseDto<Void>> postInternalProductsReleaseStock(
        @Valid @RequestBody InternalProductReleaseRequestDto requestDto
    ) {
        productService.postInternalProductsReleaseStock(requestDto);
        return ResponseEntity.ok(BaseResponseDto.success("상품 재고 차감 성공", HttpStatus.OK));
    }

}
