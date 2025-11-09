package com.gbg.deliveryservice.domain.entity;

import com.gabojago.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "p_hub_delivery")
public class HubDelivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "hub_from_id", nullable = false)
    private UUID hubFromId;

    @Column(name = "hub_to_id", nullable = false)
    private UUID hubToId;

    @Column(name = "delivery_id", nullable = false)
    private UUID deliveryId;

    @Column(name = "deliveryman_id", nullable = false)
    private UUID deliverymanId;
}
