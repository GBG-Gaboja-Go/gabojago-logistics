package com.gbg.productservice.domain.repository;

import com.gbg.productservice.domain.entity.Product;
import com.gbg.productservice.presentation.dto.request.SearchProductRequestDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID id);

    void delete(Product product);

    Page<Product> search(SearchProductRequestDto dto, Pageable pageable);

    List<Product> findAll();


}
