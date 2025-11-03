package com.gbg.userservice.domain.repository;

import com.gbg.userservice.domain.entity.User;

public interface UserRepository {

    User save(User user);
}