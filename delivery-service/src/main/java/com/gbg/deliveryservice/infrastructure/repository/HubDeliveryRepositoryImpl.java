package com.gbg.deliveryservice.infrastructure.repository;

import com.gbg.deliveryservice.domain.entity.HubDelivery;
import com.gbg.deliveryservice.domain.repository.HubDeliveryRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HubDeliveryRepositoryImpl implements HubDeliveryRepository {

    private final HubDeliveryJpaRepository hubDeliveryJpaRepository;

    @Override
    public HubDelivery save(HubDelivery hubdelivery) {
        return hubDeliveryJpaRepository.save(hubdelivery);
    }

    @Override
    public Optional<HubDelivery> findByDeliveryIdAndDeletedAtIsNull(UUID id) {
        return hubDeliveryJpaRepository.findByDeliveryIdAndDeletedAtIsNull(id);
    }

    @Override
    public List<HubDelivery> findAllByDeliverymanIdAndDeletedAtIsNull(UUID id) {
        return hubDeliveryJpaRepository.findAllByDeliverymanIdAndDeletedAtIsNull(id);
    }

    @Override
    public List<HubDelivery> findByAllDeliveryIdAndDeletedAtIsNull(UUID id) {
        return hubDeliveryJpaRepository.findAllByDeliveryIdAndDeletedAtIsNull(id);
    }


}
