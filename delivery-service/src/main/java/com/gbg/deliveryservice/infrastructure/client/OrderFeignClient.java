package com.gbg.deliveryservice.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "order-service")
public interface OrderFeignClient {

    @GetMapping("/v1/orders/{orderId}")
    void getOrderDetail(
        @PathVariable("orderId") UUID orderId,
        @RequestHeader("X-Auth-Id") String id,
        @RequestHeader("X-Auth-Role") String role
    );

}
