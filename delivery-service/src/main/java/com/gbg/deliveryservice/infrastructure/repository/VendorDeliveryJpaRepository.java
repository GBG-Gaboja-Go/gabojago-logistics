package com.gbg.deliveryservice.infrastructure.repository;

import com.gbg.deliveryservice.domain.entity.VendorDelivery;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorDeliveryJpaRepository extends JpaRepository<VendorDelivery, UUID> {

    Optional<VendorDelivery> findByDeliveryIdAndDeletedAtIsNull(UUID id);
    
    Optional<VendorDelivery> findByDeliverymanIdAndDeletedAtIsNull(UUID id);
}
