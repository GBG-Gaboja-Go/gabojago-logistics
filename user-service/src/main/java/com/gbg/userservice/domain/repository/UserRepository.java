package com.gbg.userservice.domain.repository;

import com.gbg.userservice.domain.entity.User;
import com.gbg.userservice.presentation.dto.response.UserResponseDto;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    List<User> userList();

    List<User> findAllByUsernameOrSlackEmail(String username, String slackEmail);

    Optional<User> findByUserName(String username);
}