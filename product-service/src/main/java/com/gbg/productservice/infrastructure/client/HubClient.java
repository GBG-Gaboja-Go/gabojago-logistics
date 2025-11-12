package com.gbg.productservice.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service")
public interface HubClient {

    @GetMapping("/internal/v1/hubs/{hubId}/exists")
    Boolean existsById(@PathVariable UUID hubId);
}
