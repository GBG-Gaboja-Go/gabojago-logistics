package com.gbg.hubservice.infrastructure.repository;

import com.gbg.hubservice.domain.entity.Hub;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubJpaRepository extends JpaRepository<Hub, UUID> {

    boolean existsByName(String name);
}
