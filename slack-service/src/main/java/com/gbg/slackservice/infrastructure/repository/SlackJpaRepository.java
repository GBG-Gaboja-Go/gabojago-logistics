package com.gbg.slackservice.infrastructure.repository;

import com.gbg.slackservice.domain.entity.Slack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackJpaRepository extends JpaRepository<Slack, Long> {

}
