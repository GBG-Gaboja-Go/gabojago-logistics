package com.gbg.orderservice.application.service;

import com.gbg.orderservice.infrastructure.client.DeliveryClient;
import com.gbg.orderservice.presentation.dto.OrderCreatedEvent;
import com.gbg.orderservice.presentation.dto.request.CreateDeliveryRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryNotifier {

    private final DeliveryClient deliveryClient;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {

        CreateDeliveryRequestDTO.DeliveryDTO delivery = CreateDeliveryRequestDTO.DeliveryDTO.builder()
            .orderId(event.orderId())
            .deliveryAddress(event.receiverAddress())
            .hubFromId(event.producerHubId())
            .hubToId(event.receiverHubId())
            .userFromId(event.producerVendorId())
            .userToId(event.receiverVendorId())
            .build();

        CreateDeliveryRequestDTO requestDTO = new CreateDeliveryRequestDTO(delivery);

        deliveryClient.createDelivery(requestDTO);
        log.info("delivery 생성 요청 성공");

    }
}
