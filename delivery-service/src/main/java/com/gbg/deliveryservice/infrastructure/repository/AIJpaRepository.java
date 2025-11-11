package com.gbg.deliveryservice.infrastructure.repository;

import com.gbg.deliveryservice.domain.entity.AIHistory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIJpaRepository extends JpaRepository<AIHistory, UUID> {

}
