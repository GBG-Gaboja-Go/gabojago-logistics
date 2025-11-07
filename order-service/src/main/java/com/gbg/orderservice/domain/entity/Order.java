package com.gbg.orderservice.domain.entity;

import com.gabojago.entity.BaseEntity;
import com.gbg.orderservice.domain.entity.enums.OrderStatus;
import com.gbg.orderservice.presentation.dto.response.GetOrderResponseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigInteger;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@AllArgsConstructor
@Table(name = "p_order")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "producer_vendor_id", nullable = false)
    private UUID producerVendorId;

    @Column(name = "receiver_vendor_id", nullable = false)
    private UUID receiverVendorId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total_price", nullable = false)
    private BigInteger totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "request_message", nullable = false, length = 1000)
    private String requestMessage;

    public GetOrderResponseDto toResponseDto() {
        return GetOrderResponseDto.from(this);
    }
}
