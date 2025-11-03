package com.gbg.userservice.infrasturcture.repository;

import com.gbg.userservice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

}