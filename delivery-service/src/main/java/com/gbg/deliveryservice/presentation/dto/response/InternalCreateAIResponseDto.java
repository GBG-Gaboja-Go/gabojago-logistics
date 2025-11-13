package com.gbg.deliveryservice.presentation.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class InternalCreateAIResponseDto {

    private final AIDto ai;

    @Getter
    @Builder
    public static class AIDto {

        private final UUID id;

        private final UUID orderId;

        private final String responseMessage;
    }

}
