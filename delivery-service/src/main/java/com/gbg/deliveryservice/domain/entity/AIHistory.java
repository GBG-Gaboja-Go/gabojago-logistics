package com.gbg.deliveryservice.domain.entity;

import com.gabojago.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_ai_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AIHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "order_request_message")
    private String orderRequestMessage;

    @Column(name = "slack_email")
    private String deliveryManSlackEmail;

    @Column(name = "final_deadline")
    private LocalDateTime finalDeadline;

    @Column(name = "response_message")
    private String responseMessage;

}
