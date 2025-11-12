package com.gbg.deliveryservice.infrastructure.client;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.deliveryservice.infrastructure.client.dto.GetHubResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "hub-service")
public interface HubFeignClient {

    @GetMapping("/v1/hubs/{hubId}")
    ResponseEntity<BaseResponseDto<GetHubResponseDto>> getHub(@PathVariable("hubId") UUID hubId,
        @RequestHeader("X-Auth-Id") String id,
        @RequestHeader("X-Auth-Role") String role);


    @GetMapping("/v1/hubs/manager/{hubManagerId}")
    ResponseEntity<BaseResponseDto<GetHubResponseDto>> getHubManagerId(
        @PathVariable("hubManagerId") UUID hubManagerId,
        @RequestHeader("X-Auth-Id") String id,
        @RequestHeader("X-Auth-Role") String role);

}
