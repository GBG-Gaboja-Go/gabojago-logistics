package com.gbg.hubservice.infrastructure.repository;

import com.gbg.hubservice.domain.entity.Hub;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubJpaRepository extends JpaRepository<Hub, UUID> {

    boolean existsByName(String name);

    Optional<Hub> findById(UUID id);

    Optional<Hub> findByUserId(UUID id);

    boolean existsByIdAndDeletedAtIsNull(UUID id);
}
