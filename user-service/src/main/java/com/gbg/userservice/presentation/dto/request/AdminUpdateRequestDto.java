package com.gbg.userservice.presentation.dto.request;

import com.gbg.userservice.domain.entity.UserRole;
import com.gbg.userservice.domain.entity.UserStatus;
import java.util.UUID;

public record AdminUpdateRequestDto(

    String nickname,
    UUID organization,
    String summary,
    UserRole role,
    UserStatus status

) {

}