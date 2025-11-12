package com.gbg.productservice.infrastructure.repository;

import com.gbg.productservice.domain.entity.Product;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<Product, UUID> {

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
