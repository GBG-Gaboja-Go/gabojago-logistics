package com.gbg.hubservice.infrastructure.repository;

import com.gbg.hubservice.domain.entity.HubRoute;
import com.gbg.hubservice.domain.repository.HubRouteRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubRouteRepositoryImpl implements HubRouteRepository {

    private final HubRouteJpaRepository jpa;

    @Override
    @Transactional
    public HubRoute save(HubRoute hubRoute) {
        return jpa.save(hubRoute);
    }

    @Override
    public Optional<HubRoute> findById(UUID id) {
        return jpa.findById(id).filter(r -> r.getDeletedAt() == null);
    }

    @Override
    public Page<HubRoute> findAll(Pageable pageable) {
        return jpa.findAllByDeletedAtIsNull(pageable);
    }

    @Override
    @Transactional
    public void delete(HubRoute hubRoute) {
        jpa.save(hubRoute);
    }

    @Override
    public boolean existsByStartHubIdAndEndHubId(UUID startHubId, UUID endHubId) {
        return jpa.existsByStartHubIdAndEndHubIdAndDeletedAtIsNull(startHubId, endHubId);
    }
}
