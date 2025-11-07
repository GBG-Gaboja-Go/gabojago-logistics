package com.gbg.hubservice.presentation.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateHubRouteResponseDto {

    private UUID id;

    public static CreateHubRouteResponseDto of(UUID id) {
        return CreateHubRouteResponseDto.builder().id(id).build();
    }
}
