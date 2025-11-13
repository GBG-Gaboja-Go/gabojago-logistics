package com.gbg.orderservice.presentation.dto;

import java.util.UUID;

public record OrderCreatedEvent(
    UUID orderId,
    UUID producerHubId,
    UUID producerVendorId,
    String receiverAddress,
    UUID receiverHubId,
    UUID receiverVendorId
) {

}
