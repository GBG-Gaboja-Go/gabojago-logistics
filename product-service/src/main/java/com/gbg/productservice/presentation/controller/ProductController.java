package com.gbg.productservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.productservice.application.service.ProductService;
import com.gbg.productservice.presentation.dto.request.CreateProductRequestDto;
import com.gbg.productservice.presentation.dto.request.SearchProductRequestDto;
import com.gbg.productservice.presentation.dto.request.UpdateProductRequestDto;
import com.gbg.productservice.presentation.dto.response.CreateProductResponseDto;
import com.gbg.productservice.presentation.dto.response.ProductResponseDto;
import com.gbg.productservice.presentation.dto.response.SearchProductResponseDto;
import com.gbg.productservice.presentation.dto.response.UpdateProductResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','VENDOR_MANAGER')")
    @Operation(summary = "상품 생성", description = "마스터, 허브, 업체 관리자가 상품을 생성할 수 있습니다.")
    public ResponseEntity<BaseResponseDto<CreateProductResponseDto>> createProduct(
        @Valid @RequestBody CreateProductRequestDto dto) {

        CreateProductResponseDto response = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponseDto.success("상품 생성 성공", response, HttpStatus.CREATED));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 단일 조회", description = "상품 ID로 단일 상품을 조회합니다.")
    public ResponseEntity<BaseResponseDto<ProductResponseDto>> getProductById(
        @PathVariable UUID productId) {

        ProductResponseDto response = productService.getProductById(productId);
        return ResponseEntity.ok(BaseResponseDto.success("상품 단일 조회 성공", response, HttpStatus.OK));
    }

    @GetMapping("/vendor/{vendorId}")
    @Operation(summary = "업체별 상품 목록 조회", description = "특정 업체의 상품 목록을 조회합니다.")
    public ResponseEntity<BaseResponseDto<List<ProductResponseDto>>> getProductsByVendor(
        @PathVariable UUID vendorId) {

        List<ProductResponseDto> response = productService.getProductsByVendor(vendorId);
        return ResponseEntity.ok(BaseResponseDto.success("업체별 상품 조회 성공", response, HttpStatus.OK));
    }

    @GetMapping("/search")
    @Operation(summary = "상품 검색", description = "상품명을 기준으로 검색합니다.")
    public ResponseEntity<BaseResponseDto<SearchProductResponseDto>> searchProducts(
        @ModelAttribute SearchProductRequestDto dto, Pageable pageable) {

        SearchProductResponseDto response = productService.searchProducts(dto, pageable);
        return ResponseEntity.ok(BaseResponseDto.success("상품 검색 성공", response, HttpStatus.OK));
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','VENDOR_MANAGER')")
    @Operation(summary = "상품 수정", description = "마스터, 허브, 업체 관리자가 상품을 수정할 수 있습니다.")
    public ResponseEntity<BaseResponseDto<UpdateProductResponseDto>> updateProduct(
        @PathVariable UUID productId,
        @Valid @RequestBody UpdateProductRequestDto dto) {

        UpdateProductResponseDto response = productService.updateProduct(productId, dto);
        return ResponseEntity.ok(BaseResponseDto.success("상품 수정 성공", response, HttpStatus.OK));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    @Operation(summary = "상품 삭제", description = "허브관리자와 마스터만 상품을 삭제할 수 있습니다.")
    public ResponseEntity<BaseResponseDto<Void>> deleteProduct(@PathVariable UUID productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(BaseResponseDto.success("상품 삭제 성공", HttpStatus.NO_CONTENT));
    }
}
