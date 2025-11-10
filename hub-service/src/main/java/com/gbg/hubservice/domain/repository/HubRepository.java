package com.gbg.hubservice.domain.repository;

import com.gbg.hubservice.domain.entity.Hub;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface HubRepository {

    boolean existsByName(String name);

    Optional<Hub> findById(UUID id);

    Page<Hub> findAll(Pageable pageable);

    Hub save(Hub hub);
}
