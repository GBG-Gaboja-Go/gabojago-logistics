package com.gbg.hubservice.infrastructure.repository;

import com.gbg.hubservice.domain.entity.HubRoute;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubRouteJpaRepository extends JpaRepository<HubRoute, UUID> {

    boolean existsByStartHubIdAndEndHubIdAndDeletedAtIsNull(UUID startHubId, UUID endHubId);

    Page<HubRoute> findAllByDeletedAtIsNull(Pageable pageable);
}
