package com.gbg.hubservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.hubservice.application.service.HubService;
import com.gbg.hubservice.domain.entity.Hub;
import com.gbg.hubservice.infrastructure.config.auth.CustomUser;
import com.gbg.hubservice.presentation.dto.request.CreateHubRequestDto;
import com.gbg.hubservice.presentation.dto.request.UpdateHubRequestDto;
import com.gbg.hubservice.presentation.dto.response.CreateHubResponseDto;
import com.gbg.hubservice.presentation.dto.response.GetHubResponseDto;
import com.gbg.hubservice.presentation.dto.response.GetHubsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/hubs")
public class HubController {

    private final HubService hubService;

    @PostMapping
    @Operation(summary = "허브 생성 API", description = "마스터관리자만 허브를 생성할 수 있습니다.")
    public ResponseEntity<BaseResponseDto<CreateHubResponseDto>> createHub(
        @Valid @RequestBody CreateHubRequestDto requestDto,
        Authentication authentication
    ) {
        UUID userId = ((CustomUser) authentication.getPrincipal()).getUserId();
        UUID createdId = hubService.create(requestDto, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponseDto.success("허브 생성 성공", CreateHubResponseDto.of(createdId),
                HttpStatus.CREATED));
    }

    @GetMapping
    @Operation(summary = "허브 전체 조회 API", description = "허브 목록을 조회합니다.")
    public ResponseEntity<BaseResponseDto<GetHubsResponseDto>> getHubs(Pageable pageable) {
        Page<Hub> page = hubService.getPage(pageable);
        GetHubsResponseDto body = GetHubsResponseDto.of(page);
        return ResponseEntity.ok(BaseResponseDto.success("허브 목록 조회 성공", body, HttpStatus.OK));
    }

    @GetMapping("/{hubId}")
    @Operation(summary = "허브 상세 조회 API", description = "허브 ID로 상세 정보를 조회합니다.")
    public ResponseEntity<BaseResponseDto<GetHubResponseDto>> getHub(
        @Parameter(description = "허브 UUID") @PathVariable UUID hubId
    ) {
        Hub hub = hubService.getById(hubId);
        GetHubResponseDto responseDto = GetHubResponseDto.of(
            hub.getId(), hub.getName(), hub.getAddress(), hub.getLatitude(), hub.getLongitude()
        );
        return ResponseEntity.ok(BaseResponseDto.success("허브 조회 성공", responseDto, HttpStatus.OK));
    }

    @PutMapping("/{hubId}")
    @Operation(summary = "허브 정보 수정 API", description = "허브 이름/주소/위도/경도를 수정합니다.")
    public ResponseEntity<BaseResponseDto<Void>> updateHub(
        @Parameter(description = "허브 UUID") @PathVariable UUID hubId,
        @Valid @RequestBody UpdateHubRequestDto requestDto
    ) {
        hubService.update(hubId, requestDto);
        return ResponseEntity.ok(BaseResponseDto.success("허브 수정 성공", HttpStatus.OK));
    }

    @DeleteMapping("/{hubId}")
    @Operation(summary = "허브 삭제 API", description = "허브를 삭제합니다. (소프트 삭제)")
    public ResponseEntity<BaseResponseDto<Void>> deleteHub(
        @Parameter(description = "허브 UUID") @PathVariable UUID hubId,
        Authentication authentication
    ) {
        UUID userId = ((CustomUser) authentication.getPrincipal()).getUserId();
        hubService.delete(hubId, userId);
        return ResponseEntity.ok(BaseResponseDto.success("허브 삭제 성공", HttpStatus.OK));
    }
}
