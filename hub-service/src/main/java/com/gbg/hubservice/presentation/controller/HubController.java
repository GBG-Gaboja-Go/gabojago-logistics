package com.gbg.hubservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gabojago.dto.PageResponseDto;
import com.gbg.hubservice.presentation.dto.request.CreateHubRequestDto;
import com.gbg.hubservice.presentation.dto.request.UpdateHubRequestDto;
import com.gbg.hubservice.presentation.dto.response.CreateHubResponseDto;
import com.gbg.hubservice.presentation.dto.response.GetHubResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
@RequestMapping("/v1/hubs")
public class HubController {

    @PostMapping
    @Operation(summary = "허브 생성 API", description = "마스터관리자만 허브를 생성할 수 있습니다.")
    public ResponseEntity<BaseResponseDto<CreateHubResponseDto>> createHub(
        @Valid @RequestBody CreateHubRequestDto requestDto
    ) {
        CreateHubResponseDto responseDto = CreateHubResponseDto.builder()
            .id(UUID.randomUUID())
            .build();

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponseDto.success("허브 생성 성공", responseDto, HttpStatus.CREATED));
    }

    @PostMapping("/search")
    @Operation(summary = "허브 전체 조회 API", description = "허브 목록을 페이지로 조회합니다.)")
    public ResponseEntity<BaseResponseDto<PageResponseDto<GetHubResponseDto>>> getHubs(
        Pageable pageable
    ) {
        Instant now = Instant.now();

        // 더미데이터 5개 생성
        List<GetHubResponseDto> hubs = IntStream.range(0, 5)
            .mapToObj(i -> GetHubResponseDto.builder()
                .hub(GetHubResponseDto.HubDto.builder()
                    .id(UUID.randomUUID())
                    .name("허브-" + i)
                    .address("샘플 주소 " + i)
                    .latitude(new BigDecimal("37.5665"))
                    .longitude(new BigDecimal("126.9780"))
                    .createdAt(now.minusSeconds(3600L * (i + 1)))
                    .updatedAt(now.minusSeconds(60L * (i + 1)))
                    .build())
                .build())
            .collect(Collectors.toList());

        Page<GetHubResponseDto> page = new PageImpl<>(hubs, pageable, hubs.size());

        return ResponseEntity.status(HttpStatus.OK)
            .body(
                BaseResponseDto.success("허브 목록 조회 성공", PageResponseDto.from(page), HttpStatus.OK));
    }

    @GetMapping("/{hubId}")
    @Operation(summary = "허브 상세 조회 API", description = "허브 ID로 상세 정보를 조회합니다.")
    public ResponseEntity<BaseResponseDto<GetHubResponseDto>> getHub(
        @Parameter(description = "허브 UUID") @PathVariable UUID hubId
    ) {
        GetHubResponseDto responseDto = GetHubResponseDto.builder()
            .hub(GetHubResponseDto.HubDto.builder()
                .id(hubId)
                .name("서울 중앙허브")
                .address("서울특별시 중구 세종대로 110")
                .latitude(new BigDecimal("37.5665"))
                .longitude(new BigDecimal("126.9780"))
                .createdAt(Instant.now().minusSeconds(7200))
                .updatedAt(Instant.now().minusSeconds(120))
                .build())
            .build();

        return ResponseEntity.status(HttpStatus.OK)
            .body(BaseResponseDto.success("허브 조회 성공", responseDto, HttpStatus.OK));
    }

    @PutMapping("/{hubId}")
    @Operation(summary = "허브 정보 수정 API", description = "허브 이름/주소/위도/경도를 수정합니다.")
    public ResponseEntity<BaseResponseDto<Void>> updateHub(
        @Parameter(description = "허브 UUID") @PathVariable UUID hubId,
        @Valid @RequestBody UpdateHubRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(BaseResponseDto.success("허브 수정 성공", HttpStatus.NO_CONTENT));
    }

    @DeleteMapping("/{hubId}")
    @Operation(summary = "허브 삭제 API", description = "허브를 삭제합니다.")
    public ResponseEntity<BaseResponseDto<Void>> deleteHub(
        @Parameter(description = "허브 UUID") @PathVariable UUID hubId
    ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(BaseResponseDto.success("허브 삭제 성공", HttpStatus.NO_CONTENT));
    }
}
