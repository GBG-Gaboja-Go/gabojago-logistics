package com.gbg.deliveryservice.infrastructure.repository;

import com.gbg.deliveryservice.domain.entity.VendorDelivery;
import com.gbg.deliveryservice.domain.repository.VendorDeliveryRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VendorDeliveryRepositoryImpl implements VendorDeliveryRepository {

    private final VendorDeliveryJpaRepository vendorDeliveryJpaRepository;

    @Override
    public VendorDelivery save(VendorDelivery vendorDelivery) {
        return vendorDeliveryJpaRepository.save(vendorDelivery);
    }

    @Override
    public Optional<VendorDelivery> findByDeliveryIdAndDeletedAtIsNull(UUID id) {
        return vendorDeliveryJpaRepository.findByDeliveryIdAndDeletedAtIsNull(id);
    }

    @Override
    public Optional<VendorDelivery> findByDeliverymanIdAndDeletedAtIsNull(UUID id) {
        return vendorDeliveryJpaRepository.findByDeliverymanIdAndDeletedAtIsNull(id);
    }

    @Override
    public Optional<VendorDelivery> findTopByVendorToIdOrderByCreatedAtDesc(UUID vendorId) {
        return vendorDeliveryJpaRepository.findTopByVendorToIdOrderByCreatedAtDesc(vendorId);
    }

}
