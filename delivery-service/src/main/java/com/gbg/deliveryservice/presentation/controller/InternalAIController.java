package com.gbg.deliveryservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.deliveryservice.application.service.AIService;
import com.gbg.deliveryservice.presentation.dto.request.InternalCreateAIRequestDto;
import com.gbg.deliveryservice.presentation.dto.response.GetAIResponseDto;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{orderId}")
    public ResponseEntity<BaseResponseDto<GetAIResponseDto>> getAIMessageByOrderId(
        @PathVariable UUID orderId
    ) {
        GetAIResponseDto getAIResponseDto = GetAIResponseDto.builder()
            .ai(GetAIResponseDto.AIDto.builder()
                .orderId(orderId) // 요청된 orderId 사용
                .orderRequestMessage("12월 12일 3시까지는 보내주세요!")
                .aiResponseMessage("주문 번호: " + orderId + "\n상품 정보: 마른 오징어 50박스\n[DUMMY]")
                .finalDispatchBy(LocalDateTime.of(2025, 12, 10, 9, 0))
                .build())
            .build();

        return ResponseEntity.ok(BaseResponseDto.success(
            "주문 ID 기준 AI 메시지 조회", getAIResponseDto, HttpStatus.OK
        ));
    }
}
