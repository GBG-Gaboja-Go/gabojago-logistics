package com.gbg.userservice.domain.entity;

import com.gabojago.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "p_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    private String nickname;

    private String slackId;

    private String email;

    private String password;

    private UserRole role;

    private UserStatus status;

    public static User of
        (String username, String nickname, String slackId, String email,
            String password, UserRole role)
    {
        User user = new User();
        user.username = username;
        user.nickname = nickname;
        user.slackId = slackId;
        user.email = email;
        user.password = password;
        user.role = role;
        user.status = UserStatus.PENDING;
        return user;
    }

}