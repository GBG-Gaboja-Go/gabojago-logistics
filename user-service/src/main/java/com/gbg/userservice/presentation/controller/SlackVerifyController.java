package com.gbg.userservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.userservice.application.service.SlackVerifyService;
import com.gbg.userservice.infrastructure.config.auth.CustomUser;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/slacks")
public class SlackVerifyController {

    private final SlackVerifyService slackVerifyService;

    @PostMapping
    @Transactional
    public ResponseEntity<BaseResponseDto<Void>> verifySlack(
        @AuthenticationPrincipal CustomUser customUser
    ) {
        slackVerifyService.verifySlackEmail(customUser.getId());
        return ResponseEntity
            .ok(BaseResponseDto.success(
                "슬랙 이메일 인증 완료",
                null,
                HttpStatus.OK
            ));
    }

}
