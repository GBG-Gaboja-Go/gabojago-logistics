package com.gbg.hubservice.presentation.controller;

import com.gbg.hubservice.application.service.HubService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/v1/hubs")
@RequiredArgsConstructor
public class InternalHubController {

    private final HubService hubService;

    @GetMapping("/{hubId}/exists")
    @Operation(summary = "허브 존재 여부 확인", description = "주어진 hubId의 허브가 존재하는지 확인합니다. (내부 통신용)")
    public ResponseEntity<Boolean> existsById(@PathVariable UUID hubId) {
        boolean exists = hubService.existsById(hubId);
        return ResponseEntity.ok(exists);
    }
}
