package com.gbg.userservice.domain.entity;

import com.gabojago.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    private String nickname;

    private String slackEmail;

    private String password;

    private UUID organization;

    private String summary;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeOrganization(UUID organization) {
        this.organization = organization;
    }

    public void changeSummary(String summary) {
        this.summary = summary;
    }

    private User(String username, String nickname, String slackEmail, String password, UUID organization, String summary, UserRole role, UserStatus status) {
        this.username = username;
        this.nickname = nickname;
        this.slackEmail = slackEmail;
        this.password = password;
        this.organization = organization;
        this.summary = summary;
        this.role = role;
        this.status = status;
    }

    public static User of(String username, String nickname, String slackEmail, String password, String summary) {
        return new User(
            username,
            nickname,
            slackEmail,
            password,
            new UUID(0L, 0L),
            summary,
            UserRole.USER,
            UserStatus.PENDING
        );
    }

    public static User of(String username, String nickname, String slackEmail, String password, UUID organization, String summary) {
        return new User(
            username,
            nickname,
            slackEmail,
            password,
            organization,
            summary,
            UserRole.USER,
            UserStatus.PENDING
        );
    }

}