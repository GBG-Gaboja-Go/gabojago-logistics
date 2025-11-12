package com.gbg.userservice.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gbg.userservice.domain.entity.UserRole;
import com.gbg.userservice.domain.entity.UserStatus;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {

    UserDto user;
    CompanyDto company;

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserDto {

        UUID id;
        String username;
        String nickname;
        String slackEmail;
        boolean slackVerified;
        UUID organization;
        String summary;
        UserRole role;
        UserStatus status;

    }

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CompanyDto {

    }

}
