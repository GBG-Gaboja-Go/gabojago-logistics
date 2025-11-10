package com.gbg.productservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.productservice.presentation.dto.request.CreateProductRequestDto;
import com.gbg.productservice.presentation.dto.request.UpdateProductRequestDto;
import com.gbg.productservice.presentation.dto.response.CreateProductResponseDto;
import com.gbg.productservice.presentation.dto.response.ProductResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    @PostMapping
    @Operation(summary = "상품 생성 API", description = "허브 관리자가 상품을 생성합니다.")
    public ResponseEntity<BaseResponseDto<CreateProductResponseDto>> createProduct(
        @Valid @RequestBody CreateProductRequestDto createProductRequestDto
    ) {
        UUID newProductId = UUID.randomUUID();

        CreateProductResponseDto responseDto = CreateProductResponseDto.builder()
            .product(
                CreateProductResponseDto.ProductDto.builder()
                    .id(newProductId)
                    .build()
            )
            .build();

        return ResponseEntity.ok(
            BaseResponseDto.success("상품 생성 성공", responseDto, HttpStatus.CREATED));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 단일 조회 API", description = "상품 ID로 단일 상품 정보를 조회합니다.")
    public ResponseEntity<BaseResponseDto<ProductResponseDto>> getProduct(
        @Parameter(description = "상품 UUID") @PathVariable UUID productId
    ) {
        ProductResponseDto responseDto = ProductResponseDto.builder()
            .product(
                ProductResponseDto.ProductDto.builder()
                    .id(productId)
                    .name("가자미")
                    .vendorId(UUID.randomUUID())
                    .stock(100)
                    .price(BigInteger.valueOf(10000))
                    .build()
            )
            .build();

        return ResponseEntity.ok(
            BaseResponseDto.success("상품 단일 조회 성공", responseDto, HttpStatus.OK));
    }

    @GetMapping
    @Operation(summary = "상품 목록 조회 API", description = "전체 상품 목록을 조회합니다.")
    public ResponseEntity<BaseResponseDto<List<ProductResponseDto>>> getAllProducts() {
        List<ProductResponseDto> products = List.of(
            ProductResponseDto.builder()
                .product(ProductResponseDto.ProductDto.builder()
                    .id(UUID.randomUUID())
                    .name("가자미")
                    .vendorId(UUID.randomUUID())
                    .stock(100)
                    .price(BigInteger.valueOf(12000))
                    .build())
                .build(),
            ProductResponseDto.builder()
                .product(ProductResponseDto.ProductDto.builder()
                    .id(UUID.randomUUID())
                    .name("광어")
                    .vendorId(UUID.randomUUID())
                    .stock(200)
                    .price(BigInteger.valueOf(18000))
                    .build())
                .build(),
            ProductResponseDto.builder()
                .product(ProductResponseDto.ProductDto.builder()
                    .id(UUID.randomUUID())
                    .name("연어")
                    .vendorId(UUID.randomUUID())
                    .stock(150)
                    .price(BigInteger.valueOf(15000))
                    .build())
                .build()
        );

        return ResponseEntity.ok(
            BaseResponseDto.success("상품 목록 조회 성공", products, HttpStatus.OK));
    }

    @GetMapping("/search")
    @Operation(summary = "상품 검색 API", description = "상품명으로 검색합니다.")
    public ResponseEntity<BaseResponseDto<List<ProductResponseDto>>> SearchProducts(
        @RequestParam(required = false) String name
    ) {
        List<ProductResponseDto> products = List.of(
            ProductResponseDto.builder()
                .product(ProductResponseDto.ProductDto.builder()
                    .id(UUID.randomUUID())
                    .name("가자미")
                    .vendorId(UUID.randomUUID())
                    .stock(100)
                    .price(BigInteger.valueOf(12000))
                    .build())
                .build(),
            ProductResponseDto.builder()
                .product(ProductResponseDto.ProductDto.builder()
                    .id(UUID.randomUUID())
                    .name("광어")
                    .vendorId(UUID.randomUUID())
                    .stock(200)
                    .price(BigInteger.valueOf(18000))
                    .build())
                .build(),
            ProductResponseDto.builder()
                .product(ProductResponseDto.ProductDto.builder()
                    .id(UUID.randomUUID())
                    .name("연어")
                    .vendorId(UUID.randomUUID())
                    .stock(150)
                    .price(BigInteger.valueOf(15000))
                    .build())
                .build()
        );

        // 검색 로직 (name이 null이 아니면 필터 적용)
        List<ProductResponseDto> filteredProducts = products.stream()
            .filter(product -> name == null || product.getProduct().getName().contains(name))
            .toList();

        return ResponseEntity.ok(
            BaseResponseDto.success("상품 검색 성공", filteredProducts, HttpStatus.OK)
        );
    }

    @PatchMapping("/{productId}")
    @Operation(summary = "상품 수정 API", description = "상품 정보를 수정합니다.")
    public ResponseEntity<BaseResponseDto<ProductResponseDto>> updateProduct(
        @Parameter(description = "상품 UUID") @PathVariable UUID productId,
        @Valid @RequestBody UpdateProductRequestDto requestDto
    ) {
        ProductResponseDto responseDto = ProductResponseDto.builder()
            .product(
                ProductResponseDto.ProductDto.builder()
                    .id(productId)
                    .name(
                        requestDto.getProduct().getName() != null ? requestDto.getProduct()
                            .getName() : "기존 상품명")
                    .vendorId(
                        requestDto.getProduct().getVendorId() != null ? requestDto.getProduct()
                            .getVendorId()
                            : UUID.randomUUID())
                    .stock(requestDto.getProduct().getStock() != null ? requestDto.getProduct()
                        .getStock() : 0)
                    .price(requestDto.getProduct().getPrice() != null ? requestDto.getProduct()
                        .getPrice() : BigInteger.valueOf(0))
                    .build()
            )
            .build();

        return ResponseEntity.ok(
            BaseResponseDto.success("상품 수정 성공", responseDto, HttpStatus.OK));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제 API", description = "상품을 삭제합니다.")
    public ResponseEntity<BaseResponseDto<Void>> deleteProduct(
        @Parameter(description = "업체 UUID") @PathVariable UUID productId
    ) {
        return ResponseEntity.ok(BaseResponseDto.success("상품 삭제 성공", HttpStatus.NO_CONTENT));
    }
}
