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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
    private Duration actualDeliveryTime;

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

    public void update(
        String deliveryAddress,
        double estimatedDistance,
        LocalTime estimatedTime,
        LocalDateTime startedTime,
        LocalDateTime completedTime
    ) {
        this.deliveryAddress = deliveryAddress;
        this.estimatedDistance = estimatedDistance;
        this.estimatedTime = estimatedTime;
        this.startedTime = startedTime;
        this.completedTime = completedTime;
    }

    public void updateStatus(DeliveryStatus status) {
        this.status = status;
    }

    public void startDelivery() {
        this.startedTime = LocalDateTime.now();
        this.status = DeliveryStatus.OUT_FOR_DELIVERY;
    }

    public void completedDelivery() {
        this.completedTime = LocalDateTime.now();
        this.status = DeliveryStatus.DELIVERED;
        this.actualDeliveryTime = Duration.between(startedTime, completedTime);
    }


}
