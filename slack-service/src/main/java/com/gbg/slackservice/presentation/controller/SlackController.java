package com.gbg.slackservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.slackservice.application.service.SlackService;
import com.gbg.slackservice.infrastructure.config.auth.CustomUser;
import com.gbg.slackservice.presentation.dto.request.SlackSendDmRequest;
import com.gbg.slackservice.presentation.dto.request.SlackVerifyRequest;
import com.gbg.slackservice.presentation.dto.request.SlackVerifySuccessRequest;
import com.gbg.slackservice.presentation.dto.response.SlackVerifyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/v1/slacks")
@RequiredArgsConstructor
public class SlackController {

    private final SlackService slackService;

    @GetMapping
    public String test(
        @AuthenticationPrincipal CustomUser customUser
    ) {
        log.info("[customUser : {}]", customUser.getUserId());
        return "Slack Controller Test!";
    }

    @PostMapping("/verify-member")
    public BaseResponseDto<SlackVerifyResponse> verifySlackMember(
        @RequestBody SlackVerifyRequest req
    ) {

        SlackVerifyResponse response = slackService.verifySlackMember(req.email());

        return BaseResponseDto.success(
            "슬랙 워크스페이스 멤버 확인 완료",
            response,
            HttpStatus.OK
        );
    }

    @PostMapping("/verify-success-message")
    public BaseResponseDto<Void> sendVerifySuccessMessage(
        @RequestBody SlackVerifySuccessRequest req
    ) {
        slackService.sendVerifySuccessMessage(req.channelId(), req.text());
        return BaseResponseDto.success(
            "메시지 전송 완료",
            null,
            HttpStatus.OK
        );
    }

    @PostMapping("/send-dm")
    public BaseResponseDto<String> sendDm(
        @RequestBody SlackSendDmRequest req
    ) {

        slackService.sendDm(req.email(), req.message());

        return BaseResponseDto.success(
            "Slack DM 전송 완료",
            null,
            HttpStatus.NO_CONTENT
        );
    }
}
