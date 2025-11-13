package com.gbg.deliveryservice.infrastructure.repository;

import com.gbg.deliveryservice.domain.entity.DeliveryMan;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryManJpaRepository extends JpaRepository<DeliveryMan, UUID> {

    Optional<DeliveryMan> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByHubIdAndDeletedAtIsNull(UUID hubId);

    Page<DeliveryMan> findByDeletedAtIsNull(Pageable pageable);

    Page<DeliveryMan> findByHubIdAndDeletedAtIsNull(UUID hubId, Pageable pageable);

    boolean existsByHubIdAndSequenceAndDeletedAtIsNull(UUID hubId, int sequence);

    boolean existsByIdAndDeletedAtIsNull(UUID userId);

    List<DeliveryMan> findAllByHubIdOrderBySequenceAsc(UUID hubId);

    List<DeliveryMan> findAllByHubIdIsNullOrderBySequenceAsc();
}
