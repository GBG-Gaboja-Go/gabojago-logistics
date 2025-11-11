package com.gbg.slackservice.infrastructure.client;

import com.gbg.slackservice.presentation.dto.response.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/v1/users/internal/email/{email}")
    UserResponseDto getUserByEmail(
        @PathVariable("email") String email,
        @RequestHeader("Authorization") String token);

}
