package com.gbg.orderservice.application.service.impl;

import com.gabojago.dto.BaseResponseDto;
import com.gabojago.exception.AppException;
import com.gabojago.util.PageableUtils;
import com.gbg.orderservice.application.service.OrderService;
import com.gbg.orderservice.domain.entity.Order;
import com.gbg.orderservice.domain.entity.enums.OrderStatus;
import com.gbg.orderservice.domain.repository.OrderRepository;
import com.gbg.orderservice.infrastructure.client.DeliveryClient;
import com.gbg.orderservice.infrastructure.config.auth.CustomUser;
import com.gbg.orderservice.infrastructure.resttemplate.product.client.ProductRestTemplateClient;
import com.gbg.orderservice.infrastructure.resttemplate.product.dto.request.InternalProductReleaseRequestDto;
import com.gbg.orderservice.infrastructure.resttemplate.product.dto.request.InternalProductReturnRequestDto;
import com.gbg.orderservice.infrastructure.resttemplate.product.dto.response.ProductResponseDto;
import com.gbg.orderservice.presentation.advice.OrderErrorCode;
import com.gbg.orderservice.presentation.dto.request.CreateDeliveryRequestDTO;
import com.gbg.orderservice.presentation.dto.request.CreateOrderRequestDto;
import com.gbg.orderservice.presentation.dto.request.OrderSearchRequestDto;
import com.gbg.orderservice.presentation.dto.response.CreateDeliveryResponseDTO;
import com.gbg.orderservice.presentation.dto.response.CreateOrderResponseDto;
import com.gbg.orderservice.presentation.dto.response.GetOrderResponseDto;
import java.math.BigInteger;
import java.time.LocalTime;
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
    private final DeliveryClient deliveryClient;

    @Override
    @Transactional
    public CreateOrderResponseDto createOrder(CustomUser customUser,
        CreateOrderRequestDto requestDto) {
        CreateOrderRequestDto.OrderDto orderDto = requestDto.getOrder();
        // userId로 vendor 조회
        // vendor 수령인지 공급인지.

        // product 조회
        ProductResponseDto.ProductDto product = fetchProduct(requestDto.getOrder().getProductId());
        UUID producerVendorId = product.getVendorId();

        // venoderId로 공급업체 수령 Hub가 어딘지도 알아야함

        // product 재고보다 요청 재고가 더 많은지 검증
        if (product.getStock() <= orderDto.getQuantity()) {
            throw new AppException(OrderErrorCode.ORDER_PRODUCT_OUT_OF_STOCK);
        }

        // order 생성
        Order order = Order.builder()
            .userId(UUID.fromString(customUser.getUserId())) // 사용자 uuid
            .producerHubId(UUID.randomUUID())
            .producerVendorId(producerVendorId)
            .receiverVendorId(UUID.randomUUID()) // 수령업체 uuid
            .productId(orderDto.getProductId()) // product uuid
            .quantity(orderDto.getQuantity())
            .totalPrice(
                product.getPrice().multiply(BigInteger.valueOf(orderDto.getQuantity()))
            )
            .requestMessage(orderDto.getRequestMessage())
            .status(OrderStatus.CREATED)
            .build();

        Order savedOrder = orderRepository.save(order);

        productRestTemplateClient.postInternalProductReleaseStock(
            buildReleaseStockRequest(orderDto.getProductId(),
                orderDto.getQuantity()));

        // 배송 서비스에 알림
        CreateDeliveryRequestDTO.DeliveryDTO delivery = CreateDeliveryRequestDTO.DeliveryDTO.builder()
            .orderId(savedOrder.getId())
            .deliveryAddress("서울특별시 강남구 테헤란로 123") // userVendor.getAddress()
            .hubFromId(UUID.randomUUID()) // 공급업체 hubId
            .hubToId(UUID.randomUUID()) // userVendor.getHubId()
            .userFromId(UUID.randomUUID()) // 공급업체 ID
            .userToId(UUID.randomUUID()) // 수령업체 Id userVendor.getHubId()
            .estimatedDistance(12.5)
            .estimatedTime(LocalTime.of(2, 30, 0))
            .build();
        CreateDeliveryRequestDTO requestDTO = new CreateDeliveryRequestDTO(delivery);

        BaseResponseDto<CreateDeliveryResponseDTO> response = deliveryClient.createDelivery(
            requestDTO);
        log.info("delivery 생성 요청: {}", response.getData().delivery().getId());
        return CreateOrderResponseDto.from(savedOrder);
    }

    @Override
    public Page<GetOrderResponseDto> searchOrders(CustomUser customUser,
        OrderSearchRequestDto searchRequestDto,
        Pageable pageable) {
        pageable = PageableUtils.normalize(pageable);

        OrderSearchRequestDto.OrderDto incoming =
            searchRequestDto != null ? searchRequestDto.getOrder() : null;
        OrderSearchRequestDto.OrderDto.OrderDtoBuilder builder = incoming != null
            ? OrderSearchRequestDto.OrderDto.builder()
            .userId(incoming.getUserId())
            .hubId(incoming.getHubId())
            .producerVendorId(incoming.getProducerVendorId())
            .receiverVendorId(incoming.getReceiverVendorId())
            .productId(incoming.getProductId())
            .status(incoming.getStatus())
            .dateFrom(incoming.getDateFrom())
            .dateTo(incoming.getDateTo())
            : OrderSearchRequestDto.OrderDto.builder();

        // 마스터는 모든 주문 전체 조회 가능
        if (customUser.getRole().equals("ROLE_MASTER")) {
            builder.build();
        } else if (customUser.getRole().equals("ROLE_VENDOR_MANAGER")) {
            // 수령업체는 본인 업체만 조회 가능
            builder.receiverVendorId(UUID.randomUUID());
            builder.userId(null);
        } else if (customUser.getRole().equals("ROLE_HUB_MANAGER")) {
            // 허브매니저는 본인 업체에 들어온 주문만 조회 가능
            builder.hubId(UUID.randomUUID());
        }

        UUID userId = UUID.randomUUID();
        return orderRepository.searchOrders(searchRequestDto, pageable, customUser.getRole(),
            userId);
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
        // 마스터나 허브 관리자만 배송중으로 상태 변경할 수 있음.

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
    public void postOrderCancel(CustomUser customUser, UUID orderId) {
        Order order = findOrder(orderId);
        // 마스터랑 공급업체만 취소 가능함.

        if (customUser.getRole().equals("ROLE_VENDOR_MANAGER")) {
            // 공급 업체 인지 확인
        }

        if (OrderStatus.CANCELLED.equals(order.getStatus())) {
            throw new AppException(OrderErrorCode.ORDER_ALREADY_CANCELLED);
        }

        Order cancelledOrder = order.markCancelled();
        orderRepository.save(cancelledOrder);

        // product 수량 복원
        productRestTemplateClient.postInternalProductReturnStock(
            buildReleaseReturnRequest(order.getProductId(),
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

    private InternalProductReturnRequestDto buildReleaseReturnRequest(UUID productId,
        Integer quantity) {
        return InternalProductReturnRequestDto.builder()
            .product(
                InternalProductReturnRequestDto.ProductDto.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .build()
            )
            .build();
    }

}
