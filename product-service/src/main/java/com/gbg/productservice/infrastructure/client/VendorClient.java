package com.gbg.productservice.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "vendor-service")
public interface VendorClient {

    @GetMapping("/internal/v1/vendors/{vendorId}/exists")
    Boolean existsById(@PathVariable UUID vendorId);
}
