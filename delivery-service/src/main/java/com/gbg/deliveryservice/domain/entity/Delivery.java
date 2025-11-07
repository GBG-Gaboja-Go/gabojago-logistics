package com.gbg.deliveryservice.domain.entity;

import com.gabojago.entity.BaseEntity;
import com.gbg.deliveryservice.domain.entity.enums.DeliveryStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_delivery")
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Column(name = "actual_distance")
    private double actualDistance;

    @Column(name = "actual_deliveryTime")
    private LocalTime actualDeliveryTime;

    @Column(name = "estimated_distance")
    private double estimatedDistance;

    @Column(name = "estimated_time")
    private LocalTime estimatedTime;

    @Column(name = "started_at")
    private LocalDateTime startedTime;

    @Column(name = "completed_at")
    private LocalDateTime completedTime;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;


}
