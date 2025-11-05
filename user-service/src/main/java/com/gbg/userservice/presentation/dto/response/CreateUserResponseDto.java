package com.gbg.userservice.presentation.dto.response;

import java.util.UUID;
import lombok.Builder;

@Builder
public record CreateUserResponseDto(

    UUID userId

) {

    public static CreateUserResponseDto of(UUID uuid) {
        return new CreateUserResponseDto(uuid);
    }
}