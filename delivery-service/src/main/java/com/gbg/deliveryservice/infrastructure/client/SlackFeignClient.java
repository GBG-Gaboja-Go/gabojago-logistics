package com.gbg.deliveryservice.infrastructure.client;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.deliveryservice.infrastructure.client.dto.SlackSendDmRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "slack-service")
public interface SlackFeignClient {

    @PostMapping("/v1/slacks/send-dm")
    public BaseResponseDto<String> sendDm(
        @RequestBody SlackSendDmRequest req
    );
}
