package com.gbg.slackservice.presentation.dto.request;

public record SlackSendDmRequest(
    String email,
    String message
) {

}
