package com.gbg.hubservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gabojago.dto.PageResponseDto;
import com.gbg.hubservice.presentation.dto.request.CreateHubRouteRequestDto;
import com.gbg.hubservice.presentation.dto.request.UpdateHubRouteRequestDto;
import com.gbg.hubservice.presentation.dto.response.CreateHubRouteResponseDto;
import com.gbg.hubservice.presentation.dto.response.GetHubRouteResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/hub-routes")
public class HubRouteController {

    @PostMapping
    @Operation(summary = "허브간 경로 생성", description = "마스터 관리자만 허브 경로를 생성합니다.")
    public ResponseEntity<BaseResponseDto<CreateHubRouteResponseDto>> createHubRoute(
        @Valid @RequestBody CreateHubRouteRequestDto requestDto) {
        // 더미 생성 ID 반환
        UUID createdId = UUID.randomUUID();
        return ResponseEntity.status(HttpStatus.CREATED).body(
            BaseResponseDto.success("허브 경로 생성 성공", CreateHubRouteResponseDto.of(createdId),
                HttpStatus.CREATED));
    }

    @GetMapping("/{routeId}")
    @Operation(summary = "특정 허브 경로 조회", description = "routeId로 허브 경로 상세를 조회합니다.")
    public ResponseEntity<BaseResponseDto<GetHubRouteResponseDto>> getHubRoute(
        @Parameter(description = "허브 경로 UUID") @PathVariable UUID routeId) {
        var now = Instant.now();
        var resp = GetHubRouteResponseDto.builder().route(
            GetHubRouteResponseDto.RouteDto.builder().id(routeId).startHubId(UUID.randomUUID())
                .endHubId(UUID.randomUUID()).distance(12345) // 12,345m
                .createdAt(now.minusSeconds(3600)).updatedAt(now.minusSeconds(60)).build()).build();

        return ResponseEntity.ok(BaseResponseDto.success("허브 경로 조회 성공", resp, HttpStatus.OK));
    }

    @GetMapping
    @Operation(summary = "허브 경로 조회", description = "허브 경로 목록을 페이지로 조회합니다.")
    public ResponseEntity<BaseResponseDto<PageResponseDto<GetHubRouteResponseDto>>> getHubRoutes(
        Pageable pageable) {
        var now = Instant.now();
        List<GetHubRouteResponseDto> items = IntStream.range(0, 5).mapToObj(
            i -> GetHubRouteResponseDto.builder().route(
                GetHubRouteResponseDto.RouteDto.builder().id(UUID.randomUUID())
                    .startHubId(UUID.randomUUID()).endHubId(UUID.randomUUID())
                    .distance(1000 + (i * 250)).createdAt(now.minusSeconds(7200L + i * 120L))
                    .updatedAt(now.minusSeconds(120L + i * 10L)).build()).build()).toList();

        Page<GetHubRouteResponseDto> page = new PageImpl<>(items, pageable, items.size());
        return ResponseEntity.ok(
            BaseResponseDto.success("허브 경로 목록 조회 성공", PageResponseDto.from(page), HttpStatus.OK));
    }

    @PutMapping("/{routeId}")
    @Operation(summary = "허브간 경로 정보 업데이트", description = "허브 경로 정보를 수정합니다.")
    public ResponseEntity<BaseResponseDto<Void>> updateHubRoute(
        @Parameter(description = "허브 경로 UUID") @PathVariable UUID routeId,
        @Valid @RequestBody UpdateHubRouteRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(BaseResponseDto.success("허브 경로 수정 성공", HttpStatus.NO_CONTENT));
    }

    @DeleteMapping("/{routeId}")
    @Operation(summary = "허브 경로 삭제", description = "허브 경로를 삭제합니다.")
    public ResponseEntity<BaseResponseDto<Void>> deleteHubRoute(
        @Parameter(description = "허브 경로 UUID") @PathVariable UUID routeId) {
        return ResponseEntity.ok(BaseResponseDto.success("허브 경로 삭제 성공", HttpStatus.NO_CONTENT));
    }
}
