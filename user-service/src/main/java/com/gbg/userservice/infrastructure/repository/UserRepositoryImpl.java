package com.gbg.userservice.infrastructure.repository;

import com.gbg.userservice.domain.entity.User;
import com.gbg.userservice.domain.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {

        return userJpaRepository.save(user);
    }

    @Override
    public List<User> userList() {

        return userJpaRepository.findAll();
    }

    @Override
    public List<User> findAllByUsernameOrSlackEmail(String username, String slackEmail) {

        return userJpaRepository.findAllByUsernameOrSlackEmail(username, slackEmail);
    }

    @Override
    public Optional<User> findByUserName(String username) {

        return userJpaRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(UUID userId) {

        return userJpaRepository.findById(userId);
    }

    @Override
    public Optional<User> findBySlackEmail(String email) {

        return userJpaRepository.findBySlackEmail(email);
    }
}
