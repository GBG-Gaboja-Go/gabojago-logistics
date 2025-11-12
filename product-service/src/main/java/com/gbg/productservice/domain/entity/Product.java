package com.gbg.productservice.domain.entity;

import com.gabojago.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigInteger;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private UUID vendorId;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private BigInteger price;

    @Column(nullable = false)
    private boolean isDeleted;

    public void update(String name, UUID vendorId, Integer stock, BigInteger price) {
        if (name != null) {
            this.name = name;
        }
        if (vendorId != null) {
            this.vendorId = vendorId;
        }
        if (stock != null) {
            if (stock < 0) {
                throw new IllegalArgumentException("stock cannot be negative");
            }
            this.stock = stock;
        }
        if (price != null) {
            this.price = price;
        }
    }

    public void updateStock(int newStock) {
        if (newStock < 0) {
            throw new IllegalArgumentException("stock cannot be negative");
        }
        this.stock = newStock;
    }
}
