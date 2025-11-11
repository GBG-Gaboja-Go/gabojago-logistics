package com.gbg.deliveryservice.domain.entity;

import com.gabojago.entity.BaseEntity;
import com.gbg.deliveryservice.domain.entity.enums.DeliveryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "p_deliveryman")
public class DeliveryMan extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "hub_id")
    private UUID hubId;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryType type;

    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    public void update(
        DeliveryType type,
        UUID hubId,
        Integer sequence
    ) {
        this.type = type;
        this.hubId = hubId;
        this.sequence = sequence;
    }

    public void updateHub(UUID hubId) {
        this.hubId = hubId;
    }

}
