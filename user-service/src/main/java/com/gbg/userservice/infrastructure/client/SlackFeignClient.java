package com.gbg.userservice.infrastructure.client;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.userservice.presentation.dto.request.SlackVerifyRequest;
import com.gbg.userservice.presentation.dto.response.SlackVerifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "slack-service")
public interface SlackFeignClient {

    @PostMapping("/v1/slacks/verify-member")
    BaseResponseDto<SlackVerifyResponse> verifySlackMember(
        @RequestBody SlackVerifyRequest req,
        @RequestHeader("Authorization") String token);

}
