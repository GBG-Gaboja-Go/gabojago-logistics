package com.gbg.hubservice.domain.repository;

import com.gbg.hubservice.domain.entity.HubRoute;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubRouteRepository {

    HubRoute save(HubRoute hubRoute);

    Optional<HubRoute> findById(UUID id);

    Page<HubRoute> findAll(Pageable pageable);

    void delete(HubRoute hubRoute);

    boolean existsByStartHubIdAndEndHubId(UUID startHubId, UUID endHubId);
}
