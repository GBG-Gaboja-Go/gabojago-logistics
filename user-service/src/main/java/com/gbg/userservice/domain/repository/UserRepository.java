package com.gbg.userservice.domain.repository;

import com.gbg.userservice.domain.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    List<User> userList();

    List<User> findAllByUsernameOrSlackEmail(String username, String slackEmail);

    Optional<User> findByUserName(String username);

    Optional<User> findById(UUID userId);

    Optional<User> findBySlackEmail(String email);
}
