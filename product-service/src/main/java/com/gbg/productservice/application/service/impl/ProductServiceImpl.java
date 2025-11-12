package com.gbg.productservice.application.service.impl;

import com.gabojago.exception.AppException;
import com.gbg.productservice.application.service.ProductService;
import com.gbg.productservice.domain.entity.Product;
import com.gbg.productservice.domain.repository.ProductRepository;
import com.gbg.productservice.infrastructure.client.VendorClient;
import com.gbg.productservice.presentation.advice.ProductErrorCode;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final VendorClient vendorClient;

    @Override
    @Transactional
    public CreateProductResponseDto createProduct(CreateProductRequestDto dto) {

        if (!vendorClient.existsById(dto.getProduct().getVendorId())) {
            throw new AppException(ProductErrorCode.VENDOR_NOT_FOUND);
        }
//
//        if (!hubClient.existsById(dto.getProduct().getHubId())) {
//            throw new AppException(ProductErrorCode.HUB_NOT_FOUND);
//        }

        Product product = Product.builder()
            .name(dto.getProduct().getName())
            .vendorId(dto.getProduct().getVendorId())
            .stock(dto.getProduct().getStock())
            .price(dto.getProduct().getPrice())
            .build();

        Product saved = productRepository.save(product);
        return CreateProductResponseDto.from(saved.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_FOUND));
        return ProductResponseDto.from(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByVendor(UUID vendorId) {
        return productRepository.findAll().stream()
            .filter(p -> p.getVendorId().equals(vendorId))
            .map(ProductResponseDto::from)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SearchProductResponseDto searchProducts(SearchProductRequestDto dto, Pageable pageable) {
        return SearchProductResponseDto.from(productRepository.search(dto, pageable));
    }

    @Override
    @Transactional
    public UpdateProductResponseDto updateProduct(UUID productId, UpdateProductRequestDto dto) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_FOUND));

        product.update(
            dto.getProduct().getName(),
            dto.getProduct().getVendorId(),
            dto.getProduct().getStock(),
            dto.getProduct().getPrice()
        );

        return UpdateProductResponseDto.from(product);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_FOUND));
        productRepository.delete(product); //feignClient 연동 후 softDelete로 변환 예정
    }

    @Override
    @Transactional
    public void postInternalProductReleaseStock(InternalProductReleaseRequestDto dto) {
        Product product = productRepository.findById(dto.getProduct().getProductId())
            .orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_FOUND));

        int quantity = dto.getProduct().getQuantity();
        if (product.getStock() < quantity) {
            throw new AppException(ProductErrorCode.INSUFFICIENT_STOCK);
        }
        product.updateStock(product.getStock() - quantity);
    }

    @Override
    @Transactional
    public void postInternalProductReturnStock(InternalProductReturnRequestDto dto) {
        Product product = productRepository.findById(dto.getProduct().getProductId())
            .orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_FOUND));

        int quantity = dto.getProduct().getQuantity();
        product.updateStock(product.getStock() + quantity);
    }
}
