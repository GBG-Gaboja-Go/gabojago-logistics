package com.gbg.deliveryservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.deliveryservice.application.service.AIService;
import com.gbg.deliveryservice.presentation.dto.request.InternalCreateAIRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/v1/ai")
@RequiredArgsConstructor
@Validated
public class InternalAIController {

    private final AIService aiService;

    @PostMapping("/shipping-deadline")
    public ResponseEntity<BaseResponseDto<Void>> createShippingDeadline(
        @Valid @RequestBody InternalCreateAIRequestDto requestDto
    ) {
        aiService.createShippingDeadline(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponseDto.success("최종 발송시간 생성됐습니다.", HttpStatus.CREATED));
    }
}
