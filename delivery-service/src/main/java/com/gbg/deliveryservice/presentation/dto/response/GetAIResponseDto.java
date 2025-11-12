package com.gbg.deliveryservice.presentation.dto.response;

import com.gbg.deliveryservice.domain.entity.AIHistory;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetAIResponseDto {

    private final AIDto ai;

    @Getter
    @Builder
    public static class AIDto {

        private final UUID orderId;
        private final String orderRequestMessage;
        private final String aiResponseMessage;
        private final LocalDateTime finalDeadline;

        public static GetAIResponseDto.AIDto from(AIHistory history) {
            return AIDto.builder()
                .orderId(history.getOrderId())
                .orderRequestMessage(history.getOrderRequestMessage())
                .aiResponseMessage(history.getResponseMessage())
                .finalDeadline(history.getFinalDeadline())
                .build();
        }
    }

    public static GetAIResponseDto from(AIHistory aiHistory) {
        return GetAIResponseDto.builder()
            .ai(GetAIResponseDto.AIDto.from(aiHistory))
            .build();
    }


}
