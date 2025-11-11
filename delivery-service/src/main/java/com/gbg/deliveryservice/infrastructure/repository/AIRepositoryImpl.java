package com.gbg.deliveryservice.infrastructure.repository;

import com.gbg.deliveryservice.domain.repository.AIRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AIRepositoryImpl implements AIRepository {

    private final AIJpaRepository aiJpaRepository;
}
