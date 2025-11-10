package com.gbg.orderservice.infrastructure.client;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.orderservice.presentation.dto.request.CreateDeliveryRequestDTO;
import com.gbg.orderservice.presentation.dto.response.CreateDeliveryResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {

    @PostMapping("/v1/delivery")
    BaseResponseDto<CreateDeliveryResponseDTO> createDelivery(
        @RequestBody CreateDeliveryRequestDTO requestDTO);
}
