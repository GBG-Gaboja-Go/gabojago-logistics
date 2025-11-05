package com.gbg.userservice.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.gbg.userservice.domain.entity.User;
import com.gbg.userservice.domain.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryImplTest {

    @Autowired
    private UserJpaRepository userRepository;

    @Test
    void createBaseEntityTest() {

        User user = User.of("test", "test", "test", "test", "test");

        User save =  userRepository.save(user);

        assertThat(save.getCreatedAt()).isNotNull();
    }
}