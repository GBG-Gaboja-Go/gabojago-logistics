package com.gbg.userservice.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gbg.userservice.domain.entity.UserRole;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserListResponseDto {

    UserDto user;

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserDto {

        String username;
        String nickname;
        String slackEmail;
        UUID organization;
        UserRole role;
    }
}