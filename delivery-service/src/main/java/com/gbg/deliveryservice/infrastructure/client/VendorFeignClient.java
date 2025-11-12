package com.gbg.deliveryservice.infrastructure.client;

import com.gabojago.dto.BaseResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "vendor-service")
public interface VendorFeignClient {

    @GetMapping("/v1/vendors/manager/{managerId}")
    ResponseEntity<BaseResponseDto<List<VendorResponseDto>>> getVendorsByManagerId(
        @PathVariable("managerId") UUID managerId,
        @RequestHeader("X-Auth-Id") String id,
        @RequestHeader("X-Auth-Role") String role
    );


}
