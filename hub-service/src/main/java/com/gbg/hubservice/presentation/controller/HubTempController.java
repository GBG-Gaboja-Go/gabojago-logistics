package com.gbg.hubservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.hubservice.application.service.impl.HubTempService;
import com.gbg.hubservice.presentation.dto.response.GetHubResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/hubs")
public class HubTempController {

    private final HubTempService hubService;

    @GetMapping("/manager/{hubManagerId}")
    @Operation(summary = "허브 매니저 아이디로 조회 API", description = "허브매니저 ID로 허브 ID를 조회합니다.")
    public ResponseEntity<BaseResponseDto<GetHubResponseDto>> getHub(
        @Parameter(description = "매니저 UUID") @PathVariable("hubManagerId") UUID hubManagerId) {
        GetHubResponseDto hub = hubService.getByUserId(hubManagerId);

        return ResponseEntity.ok(BaseResponseDto.success("허브 조회 성공", hub, HttpStatus.OK));
    }

}
