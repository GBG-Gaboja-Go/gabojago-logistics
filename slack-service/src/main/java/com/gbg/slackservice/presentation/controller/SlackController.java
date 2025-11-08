package com.gbg.slackservice.presentation.controller;

import com.gbg.slackservice.infrastructure.config.auth.CustomUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/slacks")
public class SlackController {

    @GetMapping
    public String test(
        @AuthenticationPrincipal CustomUser customUser
    ) {
        log.info("[customUser : {}]", customUser.getUserId());
        return "Slack Controller Test!";
    }
}
