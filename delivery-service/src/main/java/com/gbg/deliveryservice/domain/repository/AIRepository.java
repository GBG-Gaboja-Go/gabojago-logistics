package com.gbg.deliveryservice.domain.repository;

import com.gbg.deliveryservice.domain.entity.AIHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AIRepository {

    AIHistory save(AIHistory aiHistory);

    Page<AIHistory> findAll(Pageable pageable);

}
