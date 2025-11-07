package com.gbg.hubservice.domain.entity;

import com.gabojago.entity.BaseEntity;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Hub extends BaseEntity {

    private UUID id;
    private String name;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;

}
