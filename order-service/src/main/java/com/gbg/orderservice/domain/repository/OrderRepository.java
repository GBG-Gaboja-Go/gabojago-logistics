package com.gbg.orderservice.domain.repository;

import com.gbg.orderservice.domain.entity.Order;
import com.gbg.orderservice.presentation.dto.request.OrderSearchRequestDto;
import com.gbg.orderservice.presentation.dto.response.GetOrderResponseDto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(UUID orderId);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findByUserId(UUID userId, Pageable pageable);

    Page<GetOrderResponseDto> searchOrders(OrderSearchRequestDto searchRequestDto,
        Pageable pageable,
        String role, UUID userId);
}
