package com.gbg.deliveryservice.infrastructure.repository;

import com.gbg.deliveryservice.domain.entity.Delivery;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
    
}
