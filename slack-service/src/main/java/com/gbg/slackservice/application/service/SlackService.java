package com.gbg.slackservice.application.service;

import com.gbg.slackservice.presentation.dto.response.SlackVerifyResponse;

public interface SlackService {

    SlackVerifyResponse verifySlackMember(String email);


    void sendVerifySuccessMessage(String channelId, String text);
}
