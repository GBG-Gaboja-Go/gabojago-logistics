package com.gbg.deliveryservice.infrastructure.client;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.deliveryservice.infrastructure.client.dto.UserUpdateRequestDto;
import com.gbg.deliveryservice.infrastructure.config.security.CustomUser;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    @GetMapping("/v1/users/{orderId}")
    public ResponseEntity<BaseResponseDto<UUID>> userDetailUpdate(UserUpdateRequestDto req,
        CustomUser customUser
    );

}
