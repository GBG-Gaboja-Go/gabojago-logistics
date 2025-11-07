package com.gbg.orderservice.application.service.impl;

import com.gabojago.exception.AppException;
import com.gbg.orderservice.application.service.OrderService;
import com.gbg.orderservice.domain.entity.Order;
import com.gbg.orderservice.domain.entity.enums.OrderStatus;
import com.gbg.orderservice.domain.repository.OrderRepository;
import com.gbg.orderservice.presentation.advice.OrderErrorCode;
import com.gbg.orderservice.presentation.dto.request.CreateOrderRequestDto;
import com.gbg.orderservice.presentation.dto.response.CreateOrderResponseDto;
import java.math.BigInteger;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public CreateOrderResponseDto createOrder(CreateOrderRequestDto requestDto) {
        CreateOrderRequestDto.OrderDto orderDto = requestDto.getOrder();

        // 1) 재고 예약 요청 (동기)

        // vendor 조회
        // userId로 vendor에서 해당 업체 수령업체인지 확인

        // product 조회
        // product에서 해당 productId 재고 확인

        // product 재고보다 요청 재고가 더 많은지 검증
        int productQuantity = 100;
        if (productQuantity <= orderDto.getQuantity()) {
            throw new AppException(OrderErrorCode.ORDER_PRODUCT_OUT_OF_STOCK);
        }

        // product 최소 주문 수량보다 요청 재고가 적은지 검증

        // product 수량 감소 요청 : 예약 확정(비동기/동기 선택) -> confirm 또는 confirm은 consumer가 처리하도록 설계 가능

        // delivery 배송 알림

        // order 생성
        Order order = Order.builder()
            .userId(UUID.randomUUID()) // 사용자 uuid
            .producerVendorId(orderDto.getProducerVendorId())
            .receiverVendorId(UUID.randomUUID()) // 수령업체 uuid
            .deliveryId(UUID.randomUUID()) // 배송 uuid
            .productId(UUID.randomUUID()) // product uuid
            .quantity(orderDto.getQuantity())
            .totalPrice(
                BigInteger.valueOf(orderDto.getQuantity() * 1000)) // product 하나 가격 * quantity
            .requestMessage(orderDto.getRequestMessage())
            .status(OrderStatus.PENDING)
            .build();

        orderRepository.save(order);

        // 배송 서비스에 알림 (비동기 or feign)

        return CreateOrderResponseDto.builder()
            .order(CreateOrderResponseDto.OrderDto.from(order))
            .build();
    }
}
