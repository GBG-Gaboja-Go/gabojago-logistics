package com.gbg.hubservice.domain.repository;

import com.gbg.hubservice.domain.entity.HubRoute;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubRouteRepository extends JpaRepository<HubRoute, UUID> {

    HubRoute save(HubRoute route);

    Optional<HubRoute> findByIdAndDeletedAtIsNull(UUID id);

    Optional<HubRoute> findByStartHubIdAndEndHubIdAndDeletedAtIsNull(UUID startHubId,
        UUID endHubId);

    boolean existsByStartHubIdAndEndHubIdAndDeletedAtIsNull(UUID startHubId, UUID endHubId);

    Page<HubRoute> findAllByDeletedAtIsNull(Pageable pageable);
}
