package com.gbg.deliveryservice.infrastructure.repository;

import com.gbg.deliveryservice.domain.entity.AIHistory;
import com.gbg.deliveryservice.domain.repository.AIRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AIRepositoryImpl implements AIRepository {

    private final AIJpaRepository aiJpaRepository;

    @Override
    public AIHistory save(AIHistory aiHistory) {
        return aiJpaRepository.save(aiHistory);
    }

    @Override
    public Page<AIHistory> findAll(Pageable pageable) {
        return aiJpaRepository.findAll(pageable);
    }


}
