package com.gbg.deliveryservice.domain.repository;

import com.gbg.deliveryservice.domain.entity.HubDelivery;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubDeliveryRepository {

    HubDelivery save(HubDelivery hubDelivery);

    List<HubDelivery> findByAllDeliveryIdAndDeletedAtIsNull(UUID id);

    Optional<HubDelivery> findByDeliveryIdAndDeletedAtIsNull(UUID id);

    List<HubDelivery> findAllByDeliverymanIdAndDeletedAtIsNull(UUID id);

    List<HubDelivery> findAllByHubFromIdAndDeletedAtIsNull(UUID uuid);

    List<HubDelivery> findAllByHubToIdAndDeletedAtIsNull(UUID uuid);

    Optional<HubDelivery> findTopOrderByCreatedAtDesc();

    Collection<HubDelivery> findAllByDeletedAtIsNull();

}
