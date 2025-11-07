package com.gbg.orderservice.application.service;

import com.gbg.orderservice.presentation.dto.request.CreateOrderRequestDto;
import com.gbg.orderservice.presentation.dto.response.CreateOrderResponseDto;

public interface OrderService {

    CreateOrderResponseDto createOrder(CreateOrderRequestDto requestDto);
}
