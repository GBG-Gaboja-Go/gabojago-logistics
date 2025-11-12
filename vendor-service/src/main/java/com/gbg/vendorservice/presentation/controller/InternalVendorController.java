package com.gbg.vendorservice.presentation.controller;

import com.gbg.vendorservice.application.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1/vendors")
public class InternalVendorController {

    private final VendorService vendorService;

    @GetMapping("/{vendorId}/exists")
    @Operation(summary = "업체 존재 여부 확인", description = "해당 업체 ID가 존재하는지 여부를 반환합니다.")
    public Boolean existsById(@PathVariable UUID vendorId) {
        return vendorService.existsById(vendorId);
    }
}
