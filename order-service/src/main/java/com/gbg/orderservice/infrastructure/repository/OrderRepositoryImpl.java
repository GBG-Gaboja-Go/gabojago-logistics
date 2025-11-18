package com.gbg.orderservice.infrastructure.repository;

import com.gbg.orderservice.domain.entity.Order;
import com.gbg.orderservice.domain.repository.OrderRepository;
import com.gbg.orderservice.presentation.dto.request.OrderSearchRequestDto;
import com.gbg.orderservice.presentation.dto.response.GetOrderResponseDto;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderQueryDslRepository orderQueryDslRepository;

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return orderJpaRepository.findById(orderId);
    }

    @Override
    public Page<GetOrderResponseDto> searchOrders(OrderSearchRequestDto searchRequestDto,
        Pageable pageable, String role, UUID userId) {
        return orderQueryDslRepository.searchOrders(searchRequestDto, pageable, role, userId);
    }


}
