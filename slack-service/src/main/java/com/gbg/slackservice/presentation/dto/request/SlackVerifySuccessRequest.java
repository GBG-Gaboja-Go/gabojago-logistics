package com.gbg.slackservice.presentation.dto.request;

public record SlackVerifySuccessRequest(
    String channelId,
    String text
) {

}
