package com.gbg.deliveryservice.infrastructure.repository;

import com.gbg.deliveryservice.domain.entity.HubDelivery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubDeliveryJpaRepository extends JpaRepository<HubDelivery, UUID> {

    List<HubDelivery> findAllByDeliveryIdAndDeletedAtIsNull(UUID id);

    Optional<HubDelivery> findByDeliveryIdAndDeletedAtIsNull(UUID id);

    List<HubDelivery> findAllByDeliverymanIdAndDeletedAtIsNull(UUID id);
}
