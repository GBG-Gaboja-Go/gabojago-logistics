package com.gbg.deliveryservice.presentation.dto.response;

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
        private final LocalDateTime finalDispatchBy;
    }

}
