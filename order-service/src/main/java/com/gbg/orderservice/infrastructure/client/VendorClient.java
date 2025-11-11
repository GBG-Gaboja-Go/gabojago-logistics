package com.gbg.orderservice.infrastructure.client;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.orderservice.presentation.dto.response.VendorResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("vendor-service")
public interface VendorClient {

    @GetMapping("/v1/vendors/{vendorId}")
    ResponseEntity<BaseResponseDto<VendorResponseDto>> getVendor(
        @Parameter(description = "업체 UUID") @PathVariable UUID vendorId
    );
}
