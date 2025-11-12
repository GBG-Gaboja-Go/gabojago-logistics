package com.gbg.deliveryservice.infrastructure.repository;

import com.gbg.deliveryservice.domain.entity.DeliveryMan;
import com.gbg.deliveryservice.domain.repository.DeliveryManRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeliveryManRepositoryImpl implements DeliveryManRepository {

    private final DeliveryManJpaRepository deliveryManJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public DeliveryMan save(DeliveryMan deliveryman) {
        return deliveryManJpaRepository.save(deliveryman);
    }

    @Override
    public Optional<DeliveryMan> findByIdAndDeletedAtIsNull(UUID id) {
        return deliveryManJpaRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Page<DeliveryMan> findAllByHubIdAndDeletedAtIsNull(UUID hubId, Pageable pageable) {
        return deliveryManJpaRepository.findByHubIdAndDeletedAtIsNull(hubId, pageable);
    }

    @Override
    public Page<DeliveryMan> findAllAndDeletedAtIsNull(Pageable pageable) {
        return deliveryManJpaRepository.findByDeletedAtIsNull(pageable);
    }

    @Override
    public boolean existsByHubIdAndSequenceAndDeletedAtIsNull(UUID hubId, int sequence) {
        return deliveryManJpaRepository.existsByHubIdAndSequenceAndDeletedAtIsNull(hubId, sequence);
    }

    @Override
    public boolean existsByUserIdAndDeletedAtIsNull(UUID userId) {
        return deliveryManJpaRepository.existsByIdAndDeletedAtIsNull(userId);
    }

}
