package com.gbg.deliveryservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.deliveryservice.presentation.dto.request.InternalCreateAIRequestDto;
import com.gbg.deliveryservice.presentation.dto.response.GetAIResponseDto;
import com.gbg.deliveryservice.presentation.dto.response.InternalCreateAIResponseDto;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/v1/ai")
public class InternalAIController {

    @PostMapping("/shipping-deadline")
    public ResponseEntity<BaseResponseDto<InternalCreateAIResponseDto>> createAI(
        @Valid @RequestBody InternalCreateAIRequestDto internalCreateAIRequestDto
    ) {
        InternalCreateAIRequestDto.AIDto ai = internalCreateAIRequestDto.getAi();

        // 응답 ID: 요청의 orderId가 있으면 그걸 사용, 없으면 랜덤 생성
        UUID responseId = ai.getOrderId() != null ? ai.getOrderId() : UUID.randomUUID();

        // 추후 ai가 추천한 최종발송시한
        LocalDateTime sampleFinalDispatchBy = LocalDateTime.now().plusHours(3);

        // 메시지 생성 (workTime이 null이면 내부에서 기본값 대체)
        String responseMessage = buildDummyResponseMessage(ai, sampleFinalDispatchBy);

        // 응답 DTO 조립
        InternalCreateAIResponseDto.AIDto responseAi = InternalCreateAIResponseDto.AIDto.builder()
            .id(responseId)
            .responseMessage(responseMessage)
            .build();

        InternalCreateAIResponseDto responseDto = InternalCreateAIResponseDto.builder()
            .ai(responseAi)
            .build();

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponseDto.success("ai 메시지 입니다.", responseDto, HttpStatus.CREATED));
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

    private String buildDummyResponseMessage(InternalCreateAIRequestDto.AIDto ai,
        LocalDateTime finalDispatchBy) {

        InternalCreateAIRequestDto.AIDto.WorkTime effectiveWorkTime =
            ai.getDriverWorkTime() != null
                ? ai.getDriverWorkTime()
                : InternalCreateAIRequestDto.AIDto.WorkTime.builder()
                    .startHour(9)
                    .endHour(18)
                    .build();

        StringBuilder sb = new StringBuilder();
        sb.append("주문 번호 : ").append(ai.getOrderId()).append("\n");
        sb.append("주문자 정보 : ").append(ai.getOrdererName())
            .append(" / ").append(ai.getOrdererEmail()).append("\n");
        sb.append("주문 시간 : ").append(ai.getOrderTime()).append("\n");
        sb.append("상품 정보 : ").append(ai.getProductName())
            .append(" ").append(ai.getProductQuantity()).append("박스").append("\n");

        if (ai.getOrderRequestMessage() != null && !ai.getOrderRequestMessage().isBlank()) {
            sb.append("요청 사항 : ").append(ai.getOrderRequestMessage()).append("\n");
        }

        if (ai.getPickup() != null) {
            sb.append("발송지 : ").append(ai.getPickup().getName()).append("\n");
        }

        if (ai.getVia() != null && !ai.getVia().isEmpty()) {
            sb.append("경유지 : ");
            for (int i = 0; i < ai.getVia().size(); i++) {
                sb.append(ai.getVia().get(i).getName());
                if (i < ai.getVia().size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }

        if (ai.getDestination() != null) {
            sb.append("도착지 : ").append(ai.getDestination().getAddress() != null
                ? ai.getDestination().getAddress()
                : ai.getDestination().getName()).append("\n");
        }

        sb.append("배송담당자 : ").append(ai.getDeliveryPersonName())
            .append(" / ").append(ai.getDeliveryPersonEmail()).append("\n");

        if (finalDispatchBy != null) {
            sb.append("\n위 내용을 기반으로 도출된 최종 발송 시한은 ")
                .append(finalDispatchBy)
                .append(" 입니다.");
        }

        return sb.toString();
    }
}
