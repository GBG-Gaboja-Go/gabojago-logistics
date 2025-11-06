package com.gbg.userservice.infrastructure.repository;

import com.gbg.userservice.domain.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    List<User> findAllByUsernameOrSlackEmail(String username, String slackEmail);

    Optional<User> findByUsername(String username);

    Optional<User> findById(UUID userId);
}