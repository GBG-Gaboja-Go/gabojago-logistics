package com.gbg.userservice.presentation.dto.request;

public record SlackVerifySuccessRequest(
    String channelId,
    String text
) {

}
