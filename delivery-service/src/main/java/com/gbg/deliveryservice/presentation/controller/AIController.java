package com.gbg.deliveryservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.deliveryservice.application.service.AIService;
import com.gbg.deliveryservice.presentation.dto.response.GetAIResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/ai")
public class AIController {

    private final AIService aiService;

    @GetMapping("/logs")
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<BaseResponseDto<Page<GetAIResponseDto>>> getAllLogs(
        Pageable pageable
    ) {
        Page<GetAIResponseDto> allLogs = aiService.getAllLogs(pageable);
        return ResponseEntity.ok(
            BaseResponseDto.success("ai 로그 전체 조회", allLogs, HttpStatus.OK));
    }

}
