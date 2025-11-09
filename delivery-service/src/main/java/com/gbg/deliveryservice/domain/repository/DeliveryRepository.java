package com.gbg.deliveryservice.domain.repository;

import com.gbg.deliveryservice.domain.entity.Delivery;
import com.gbg.deliveryservice.domain.entity.enums.DeliveryStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRepository {

    Delivery save(Delivery delivery);

    Optional<Delivery> findByIdAndDeletedAtIsNull(UUID id);

    Page<Delivery> deliveryPage(Pageable pageable, DeliveryStatus status);

    Page<Delivery> deliveryMyPage(Pageable pageable, DeliveryStatus status,
        List<UUID> deliveryIdList);
}
