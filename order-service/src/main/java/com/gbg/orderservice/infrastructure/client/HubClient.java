package com.gbg.orderservice.infrastructure.client;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.orderservice.presentation.dto.response.GetHubResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("hub-service")
public interface HubClient {

    @GetMapping("/v1/hubs/manager/{hubManagerId}")
    public ResponseEntity<BaseResponseDto<GetHubResponseDto>> getHubManagerId(
        @Parameter(description = "매니저 UUID") @PathVariable("hubManagerId") UUID hubManagerId);
}
