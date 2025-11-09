package com.gbg.slackservice.presentation.dto.response;

public record SlackVerifyResponse(
    boolean ok,
    String slackId
) {

}
