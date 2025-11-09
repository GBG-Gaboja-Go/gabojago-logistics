package com.gbg.hubservice.domain.entity;

import com.gabojago.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_hub")
@Where(clause = "deleted_at IS NULL")
public class Hub extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "user_id", nullable = false, columnDefinition = "uuid")
    private UUID userId;

    public void update(String name, String address, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
