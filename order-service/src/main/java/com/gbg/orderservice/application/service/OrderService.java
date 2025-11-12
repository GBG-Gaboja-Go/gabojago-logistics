package com.gbg.orderservice.application.service;

import com.gbg.orderservice.infrastructure.config.auth.CustomUser;
import com.gbg.orderservice.presentation.dto.request.CreateOrderRequestDto;
import com.gbg.orderservice.presentation.dto.request.OrderSearchRequestDto;
import com.gbg.orderservice.presentation.dto.response.CreateOrderResponseDto;
import com.gbg.orderservice.presentation.dto.response.GetOrderResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    CreateOrderResponseDto createOrder(CustomUser customUser, CreateOrderRequestDto requestDto);

    GetOrderResponseDto getOrder(UUID orderId);

    Page<GetOrderResponseDto> searchOrders(CustomUser customUser,
        OrderSearchRequestDto searchRequestDto,
        Pageable pageable);

    void postInternalOrderDelivering(CustomUser customUser, UUID orderId);

    void postInternalOrderDelivered(CustomUser customUser, UUID orderId);

    void postOrderCancel(CustomUser customUser, UUID orderId);
}
