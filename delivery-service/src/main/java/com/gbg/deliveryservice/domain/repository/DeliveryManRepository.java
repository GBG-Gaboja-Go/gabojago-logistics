package com.gbg.deliveryservice.domain.repository;

import com.gbg.deliveryservice.domain.entity.DeliveryMan;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryManRepository {

    DeliveryMan save(DeliveryMan deliveryman);

    Optional<DeliveryMan> findByIdAndDeletedAtIsNull(UUID id);

    Page<DeliveryMan> findAllByHubIdAndDeletedAtIsNull(UUID hubId, Pageable pageable);

    Page<DeliveryMan> findAllAndDeletedAtIsNull(Pageable pageable);

    boolean existsByHubIdAndSequenceAndDeletedAtIsNull(UUID hubId, int sequence);
}
