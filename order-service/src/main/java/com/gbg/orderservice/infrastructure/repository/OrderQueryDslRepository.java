package com.gbg.orderservice.infrastructure.repository;

import com.gbg.orderservice.presentation.dto.request.OrderSearchRequestDto;
import com.gbg.orderservice.presentation.dto.response.GetOrderResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderQueryDslRepository {

    Page<GetOrderResponseDto> searchOrders(OrderSearchRequestDto searchRequestDto,
        Pageable pageable,
        String role, UUID userId);

}
