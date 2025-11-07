package com.gbg.userservice.presentation.dto.request;

import java.util.UUID;

public record UserUpdateRequestDto(

    String nickname,
    UUID organization,
    String summary

) {

}