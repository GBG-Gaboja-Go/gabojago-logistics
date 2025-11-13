package com.gbg.orderservice.application.service.impl;

import com.gabojago.dto.BaseResponseDto;
import com.gabojago.exception.AppException;
import com.gabojago.util.PageableUtils;
import com.gbg.orderservice.application.service.OrderService;
import com.gbg.orderservice.domain.entity.Order;
import com.gbg.orderservice.domain.entity.enums.OrderStatus;
import com.gbg.orderservice.domain.repository.OrderRepository;
import com.gbg.orderservice.infrastructure.client.DeliveryClient;
import com.gbg.orderservice.infrastructure.client.HubClient;
import com.gbg.orderservice.infrastructure.client.VendorClient;
import com.gbg.orderservice.infrastructure.config.auth.CustomUser;
import com.gbg.orderservice.infrastructure.resttemplate.product.client.ProductRestTemplateClient;
import com.gbg.orderservice.infrastructure.resttemplate.product.dto.request.InternalProductReleaseRequestDto;
import com.gbg.orderservice.infrastructure.resttemplate.product.dto.request.InternalProductReturnRequestDto;
import com.gbg.orderservice.infrastructure.resttemplate.product.dto.response.ProductResponseDto;
import com.gbg.orderservice.presentation.advice.OrderErrorCode;
import com.gbg.orderservice.presentation.dto.OrderCreatedEvent;
import com.gbg.orderservice.presentation.dto.request.CreateOrderRequestDto;
import com.gbg.orderservice.presentation.dto.request.OrderSearchRequestDto;
import com.gbg.orderservice.presentation.dto.response.CreateOrderResponseDto;
import com.gbg.orderservice.presentation.dto.response.GetHubResponseDto;
import com.gbg.orderservice.presentation.dto.response.GetOrderResponseDto;
import com.gbg.orderservice.presentation.dto.response.VendorResponseDto;
import java.math.BigInteger;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRestTemplateClient productRestTemplateClient;
    private final DeliveryClient deliveryClient;
    private final VendorClient vendorClient;
    private final HubClient hubClient;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public CreateOrderResponseDto createOrder(CustomUser customUser,
        CreateOrderRequestDto requestDto) {
        CreateOrderRequestDto.OrderDto orderDto = requestDto.getOrder();
        UUID customerVendorId = orderDto.getCustomerVendorId();
        UUID productId = orderDto.getProductId();
        Integer requestedQuantity = orderDto.getQuantity();

        // vendor 수령업체 검증
        VendorResponseDto.VendorDto receiverVendor = fetchVendorById(customerVendorId);

//        log.info("reciverVendorId: {}", receiverVendor.isReceiver());
//        validateIsReceiverVendor(receiverVendor);

        // product 조회, 재고 검증
        ProductResponseDto.ProductDto product = fetchProduct(productId);
        validateProductStock(product.getStock(), requestedQuantity);

        // vendor 공급업체 검증
        UUID producerVendorId = product.getVendorId();
        VendorResponseDto.VendorDto producerVendor = fetchVendorById(producerVendorId);
        // validateIsProducerVendor(producerVendor);

        UUID producerHubId = producerVendor.getHubId();

        // order 생성
        Order order = Order.builder()
            .userId(UUID.fromString(customUser.getUserId()))
            .producerHubId(producerHubId) // 공급 hub id
            .producerVendorId(producerVendorId) // 공급업체 uuid
            .producerVendorManagerId(producerVendor.getVendorManagerId())
            .receiverVendorId(receiverVendor.getId()) // 수령업체 uuid
            .productId(productId)
            .quantity(requestedQuantity)
            .totalPrice(
                product.getPrice().multiply(BigInteger.valueOf(requestedQuantity))
            )
            .requestMessage(orderDto.getRequestMessage())
            .status(OrderStatus.CREATED)
            .build();

        Order savedOrder = orderRepository.save(order);

        // 상품 재고 차감
        releaseProductStock(orderDto.getProductId(), orderDto.getQuantity());

        // 배송 서비스에 알림
        eventPublisher.publishEvent(new OrderCreatedEvent(
            savedOrder.getId(),
            producerHubId,
            producerVendorId,
            receiverVendor.getAddress(),
            receiverVendor.getHubId(),
            receiverVendor.getId()
        ));

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
    public void postInternalOrderDelivering(CustomUser customUser, UUID orderId) {
        Order order = findOrder(orderId);

        if (customUser.getRole().equals("ROLE_HUB_MANAGER")) {
            validateHubManagerAccess(customUser, order.getProducerHubId());
        }

        if (OrderStatus.DELIVERING.equals(order.getStatus())) {
            throw new AppException(OrderErrorCode.ORDER_ALREADY_DELIVERING);
        }

        Order cancelledOrder = order.markDelivering();
        orderRepository.save(cancelledOrder);
    }

    @Override
    @Transactional
    public void postInternalOrderDelivered(CustomUser customUser, UUID orderId) {
        Order order = findOrder(orderId);

        if (customUser.getRole().equals("ROLE_HUB_MANAGER")) {
            validateHubManagerAccess(customUser, order.getProducerHubId());
        }

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
            UUID currentUserId = UUID.fromString(customUser.getUserId());
            if (!order.getProducerVendorManagerId().equals(currentUserId)) {
                throw new AppException(OrderErrorCode.ORDER_ACCESS_DENIED_VENDOR);
            }
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

    private VendorResponseDto.VendorDto fetchVendorById(UUID vendorId) {
        ResponseEntity<BaseResponseDto<VendorResponseDto>> response = vendorClient.getVendor(
            vendorId);
        return response.getBody().getData().getVendor();
    }

    private void validateIsReceiverVendor(VendorResponseDto.VendorDto vendor) {
        if (!vendor.isReceiver()) {
            throw new AppException(OrderErrorCode.ORDER_ONLY_RECEIVER_CAN_ORDER);
        }
    }

    private void validateIsProducerVendor(VendorResponseDto.VendorDto vendor) {
        if (!vendor.isSupplier()) {
            throw new AppException(OrderErrorCode.ORDER_SUPPLIER_VENDOR_ROLE_INVALID);
        }
    }

    private void validateProductStock(Integer currentStock, Integer requestedQuantity) {
        if (currentStock < requestedQuantity) {
            throw new AppException(OrderErrorCode.ORDER_PRODUCT_OUT_OF_STOCK);
        }
    }

    private void validateHubManagerAccess(CustomUser customUser, UUID orderHubId) {
        ResponseEntity<BaseResponseDto<GetHubResponseDto>> response = hubClient.getHubManagerId(
            UUID.fromString(customUser.getUserId()));

        GetHubResponseDto.HubDto hubDto = response.getBody()
            .getData()
            .getHub();

        // 관리 허브 존재 여부 검증
        if (hubDto == null) {
            throw new AppException(OrderErrorCode.ORDER_HUB_MANAGER_NO_HUB);
        }

        // 담당 허브 맞는지 검증
        if (!hubDto.getId().equals(orderHubId)) {
            throw new AppException(OrderErrorCode.ORDER_HUB_MANAGER_NO_HUB);
        }

    }

    private void releaseProductStock(UUID productId, Integer quantity) {
        productRestTemplateClient.postInternalProductReleaseStock(
            buildReleaseStockRequest(productId, quantity)
        );
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
