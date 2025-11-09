package com.gbg.orderservice.application.service.impl;

import com.gabojago.exception.AppException;
import com.gabojago.util.PageableUtils;
import com.gbg.orderservice.application.service.OrderService;
import com.gbg.orderservice.domain.entity.Order;
import com.gbg.orderservice.domain.entity.enums.OrderStatus;
import com.gbg.orderservice.domain.repository.OrderRepository;
import com.gbg.orderservice.infrastructure.resttemplate.product.client.ProductRestTemplateClient;
import com.gbg.orderservice.infrastructure.resttemplate.product.dto.request.InternalProductReleaseRequestDto;
import com.gbg.orderservice.infrastructure.resttemplate.product.dto.response.ProductResponseDto;
import com.gbg.orderservice.presentation.advice.OrderErrorCode;
import com.gbg.orderservice.presentation.dto.request.CreateOrderRequestDto;
import com.gbg.orderservice.presentation.dto.request.OrderSearchRequestDto;
import com.gbg.orderservice.presentation.dto.response.CreateOrderResponseDto;
import com.gbg.orderservice.presentation.dto.response.GetOrderResponseDto;
import java.math.BigInteger;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRestTemplateClient productRestTemplateClient;

    @Override
    @Transactional
    public CreateOrderResponseDto createOrder(CreateOrderRequestDto requestDto) {
        CreateOrderRequestDto.OrderDto orderDto = requestDto.getOrder();

        // 1) 재고 예약 요청 (동기)

        // vendor 조회
        // userId로 vendor에서 해당 업체 수령업체인지 확인

        // product 조회
        ProductResponseDto.ProductDto product = fetchProduct(requestDto.getOrder().getProductId());

        // product 재고보다 요청 재고가 더 많은지 검증
        if (product.getStock() <= orderDto.getQuantity()) {
            throw new AppException(OrderErrorCode.ORDER_PRODUCT_OUT_OF_STOCK);
        }

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
            .status(OrderStatus.CREATED)
            .build();

        Order savedOrder = orderRepository.save(order);

        // product 수량 감소 요청 : 예약 확정(비동기/동기 선택) -> confirm 또는 confirm은 consumer가 처리하도록 설계 가능
        productRestTemplateClient.postInternalProductReleaseStock(
            buildReleaseStockRequest(orderDto.getProductId(),
                orderDto.getQuantity()));

        // 배송 서비스에 알림 (비동기 or feign)

        return CreateOrderResponseDto.from(savedOrder);
    }

    @Override
    public Page<GetOrderResponseDto> searchOrders(OrderSearchRequestDto searchRequestDto,
        Pageable pageable) {
        String role = "";
        UUID userId = UUID.randomUUID();
        pageable = PageableUtils.normalize(pageable);
        return orderRepository.searchOrders(searchRequestDto, pageable, role, userId);
    }

    @Override
    public GetOrderResponseDto getOrder(UUID orderId) {
        Order order = findOrder(orderId);
        return GetOrderResponseDto.from(order);
    }

    @Override
    @Transactional
    public void postInternalOrderDelivering(UUID orderId) {
        Order order = findOrder(orderId);
        // 권한 검증

        if (OrderStatus.DELIVERING.equals(order.getStatus())) {
            throw new AppException(OrderErrorCode.ORDER_ALREADY_DELIVERING);
        }

        Order cancelledOrder = order.markDelivering();
        orderRepository.save(cancelledOrder);
    }

    @Override
    @Transactional
    public void postInternalOrderDelivered(UUID orderId) {
        Order order = findOrder(orderId);
        // 권한 검증

        if (OrderStatus.DELIVERED.equals(order.getStatus())) {
            throw new AppException(OrderErrorCode.ORDER_ALREADY_DELIVERED);
        }

        Order cancelledOrder = order.markDelivered();
        orderRepository.save(cancelledOrder);
    }

    @Override
    @Transactional
    public void postOrderCancel(UUID orderId) {
        Order order = findOrder(orderId);
        // 권한 검증

        if (OrderStatus.CANCELLED.equals(order.getStatus())) {
            throw new AppException(OrderErrorCode.ORDER_ALREADY_CANCELLED);
        }

        Order cancelledOrder = order.markCancelled();
        orderRepository.save(cancelledOrder);

        // product 수량 복원
        productRestTemplateClient.postInternalProductReturnStock(
            buildReleaseStockRequest(order.getProductId(),
                order.getQuantity()));
    }

    private Order findOrder(UUID orderId) {
        return orderRepository.findById(orderId)
            .filter(o -> o.getDeletedAt() == null)
            .orElseThrow(() -> new AppException(OrderErrorCode.ORDER_NOT_FOUND));
    }

    private ProductResponseDto.ProductDto fetchProduct(UUID productId) {
        ProductResponseDto response = productRestTemplateClient.getProduct(productId);
        if (response == null || response.getProduct() == null) {
            throw new AppException(OrderErrorCode.ORDER_PRODUCT_NOT_FOUND);
        }
        return response.getProduct();
    }

    private InternalProductReleaseRequestDto buildReleaseStockRequest(UUID productId,
        Integer quantity) {
        return InternalProductReleaseRequestDto.builder()
            .product(
                InternalProductReleaseRequestDto.ProductDto.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .build()
            )
            .build();
    }

}
