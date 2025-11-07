package com.gbg.hubservice.domain.entity;

import com.gabojago.entity.BaseEntity;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class HubRoute extends BaseEntity {

    private UUID id;
    private UUID startHubId;
    private UUID endHubId;
    private double distance;

}
