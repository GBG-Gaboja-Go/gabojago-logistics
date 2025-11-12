package com.gbg.deliveryservice.infrastructure.client.dto;

import lombok.Builder;

@Builder
public record SlackSendDmRequest(
    String email,
    String message
) {

}
