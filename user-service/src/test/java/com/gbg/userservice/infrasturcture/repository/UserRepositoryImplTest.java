package com.gbg.userservice.infrasturcture.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.gbg.userservice.domain.entity.User;
import com.gbg.userservice.domain.entity.UserRole;
import com.gbg.userservice.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class UserRepositoryImplTest {

    @Autowired
    private UserJpaRepository userRepository;

    @Test
    void createBaseEntityTest() {

        User user = User.of("test", "test", "test", "test", "test", UserRole.MASTER);

        User save =  userRepository.save(user);

        assertThat(save.getCreatedAt()).isNotNull();
    }
}