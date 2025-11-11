package com.gbg.deliveryservice.infrastructure.client.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetHubResponseDto {

    private HubDto hub;

    @Getter
    @NoArgsConstructor
    public static class HubDto {

        private UUID id;
    }
}
