package com.gbg.slackservice.application.service;

import com.gbg.slackservice.presentation.dto.response.SlackResponseDto;
import com.gbg.slackservice.presentation.dto.response.SlackVerifyResponse;
import java.util.List;

public interface SlackService {

    SlackVerifyResponse verifySlackMember(String email);


    void sendVerifySuccessMessage(String channelId, String text);

    void sendDm(String email, String message);

    SlackResponseDto slackLogs();
}
