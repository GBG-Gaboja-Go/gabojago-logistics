package com.gbg.deliveryservice.domain.repository;

import com.gbg.deliveryservice.domain.entity.VendorDelivery;
import java.util.Optional;
import java.util.UUID;

public interface VendorDeliveryRepository {

    VendorDelivery save(VendorDelivery vendorDelivery);

    Optional<VendorDelivery> findByDeliveryIdAndDeletedAtIsNull(UUID id);

    Optional<VendorDelivery> findByDeliverymanIdAndDeletedAtIsNull(UUID id);

    Optional<VendorDelivery> findTopByVendorToIdOrderByCreatedAtDesc(UUID vendorId);

}
