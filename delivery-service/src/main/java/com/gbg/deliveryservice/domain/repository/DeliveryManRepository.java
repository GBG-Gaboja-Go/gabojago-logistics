package com.gbg.deliveryservice.domain.repository;

import com.gbg.deliveryservice.domain.entity.DeliveryMan;
import jakarta.validation.constraints.NotNull;
import java.util.List;
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

    boolean existsByUserIdAndDeletedAtIsNull(@NotNull(message = "userId를 입력해주세요") UUID userId);

    List<DeliveryMan> findAllByHubIdOrderBySequenceAsc(UUID hubId);

    List<DeliveryMan> findAllByHubIdIsNullOrderBySequenceAsc();
}
