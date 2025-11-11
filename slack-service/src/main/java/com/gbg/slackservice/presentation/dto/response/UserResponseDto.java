package com.gbg.slackservice.presentation.dto.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    UUID id;
    String username;
    String nickname;
    String slackEmail;
    boolean slackVerified;
    UUID organization;
    String summary;
    String role;
    String status;

}
