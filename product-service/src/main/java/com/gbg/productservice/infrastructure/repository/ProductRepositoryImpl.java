package com.gbg.productservice.infrastructure.repository;

import com.gbg.productservice.domain.entity.Product;
import com.gbg.productservice.domain.repository.ProductRepository;
import com.gbg.productservice.presentation.dto.request.SearchProductRequestDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;


    @Override
    public Product save(Product product) {
        return jpaProductRepository.save(product);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaProductRepository.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return jpaProductRepository.findAll();
    }

    @Override
    public void delete(Product product) {
        jpaProductRepository.delete(product);
    }

    @Override
    public Page<Product> search(SearchProductRequestDto dto, Pageable pageable) {
        if (dto.getName() != null && !dto.getName().isEmpty()) {
            return jpaProductRepository.findByNameContainingIgnoreCase(dto.getName(), pageable);
        }
        return jpaProductRepository.findAll(pageable);
    }
}
