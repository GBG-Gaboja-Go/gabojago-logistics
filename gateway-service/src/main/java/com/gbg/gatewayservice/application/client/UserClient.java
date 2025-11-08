package com.gbg.gatewayservice.application.client;

import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserClient {

    @GetMapping("/v1/users/internal/{userId}/status")
    String getUserStatus(@PathVariable("userId")UUID userId);

}
