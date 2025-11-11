package com.gbg.slackservice.domain.entity;

import com.gabojago.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_slacks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Slack extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String receiverId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean success = false;

    public void updateSuccess(boolean success) {
        this.success = success;
    }

    private Slack(String receiverId, String content, boolean success) {
        this.receiverId = receiverId;
        this.content = content;
        this.success = success;
    }

    public static Slack of(String receiverId, String content) {
        return new Slack(receiverId, content, false);
    }

}
