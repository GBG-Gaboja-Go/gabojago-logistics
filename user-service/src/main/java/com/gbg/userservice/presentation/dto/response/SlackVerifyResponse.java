package com.gbg.userservice.presentation.dto.response;

public record SlackVerifyResponse(
    boolean ok,
    String slackId
) {

}
