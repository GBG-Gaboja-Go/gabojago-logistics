package com.gbg.hubservice.infrastructure.repository;

import com.gbg.hubservice.domain.entity.Hub;
import com.gbg.hubservice.domain.repository.HubRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepository {

    private final HubJpaRepository jpa;

    @Override
    public boolean existsByName(String name) {
        return jpa.existsByName(name);
    }

    @Override
    public Optional<Hub> findById(UUID id) {
        return jpa.findById(id);
    }

    @Override
    public Page<Hub> findAll(Pageable pageable) {
        return jpa.findAll(pageable);
    }

    @Override
    public Hub save(Hub hub) {
        return jpa.save(hub);
    }
}
