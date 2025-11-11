package com.gbg.hubservice.application.service;

import com.gbg.hubservice.domain.entity.HubRoute;
import com.gbg.hubservice.presentation.dto.request.CreateHubRouteRequestDto;
import com.gbg.hubservice.presentation.dto.request.UpdateHubRouteRequestDto;
import com.gbg.hubservice.presentation.dto.response.GetHubRouteResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubRouteService {

    UUID create(CreateHubRouteRequestDto request, UUID actorId);

    GetHubRouteResponseDto getById(UUID routeId);

    Page<HubRoute> getPage(Pageable pageable);

    void update(UUID routeId, UpdateHubRouteRequestDto request, UUID actorId);

    void delete(UUID routeId, UUID actorId);
}
