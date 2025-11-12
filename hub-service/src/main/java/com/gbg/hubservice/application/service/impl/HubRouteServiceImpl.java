package com.gbg.hubservice.application.service.impl;

import com.gabojago.exception.AppException;
import com.gabojago.util.PageableUtils;
import com.gbg.hubservice.application.service.HubRouteService;
import com.gbg.hubservice.application.service.exception.HubErrorCode;
import com.gbg.hubservice.domain.entity.Hub;
import com.gbg.hubservice.domain.entity.HubRoute;
import com.gbg.hubservice.domain.repository.HubRepository;
import com.gbg.hubservice.domain.repository.HubRouteRepository;
import com.gbg.hubservice.infrastructure.config.cache.CacheNames;
import com.gbg.hubservice.infrastructure.config.kakao.KakaoDirectionsClient;
import com.gbg.hubservice.presentation.dto.request.CreateHubRouteRequestDto;
import com.gbg.hubservice.presentation.dto.request.UpdateHubRouteRequestDto;
import com.gbg.hubservice.presentation.dto.response.GetHubRouteResponseDto;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubRouteServiceImpl implements HubRouteService {

    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;
    private final KakaoDirectionsClient directions;

    private Hub getHubOrThrow(UUID id) {
        return hubRepository.findById(id)
            .orElseThrow(() -> new AppException(HubErrorCode.HUB_NOT_FOUND));
    }

    private KakaoDirectionsClient.RouteSummary calcIfNeeded(Double distanceKm, Integer durationMin,
        Hub start, Hub end) {

        if (distanceKm != null && durationMin != null) {
            return new KakaoDirectionsClient.RouteSummary(distanceKm, durationMin);
        }
        BigDecimal sLat = start.getLatitude();
        BigDecimal sLon = start.getLongitude();
        BigDecimal eLat = end.getLatitude();
        BigDecimal eLon = end.getLongitude();

        if (sLat == null || sLon == null || eLat == null || eLon == null) {
            throw new AppException(HubErrorCode.INVALID_COORDINATES);
        }
        return directions.getSummary(sLat.doubleValue(), sLon.doubleValue(), eLat.doubleValue(),
            eLon.doubleValue());
    }

    @Override
    @Transactional
    public UUID create(CreateHubRouteRequestDto request, UUID actorId) {
        var dto = request.getRoute();
        UUID startId = dto.getStartHubId();
        UUID endId = dto.getEndHubId();

        if (Objects.equals(startId, endId)) {
            throw new AppException(HubErrorCode.SAME_START_END_HUB);
        }
        if (hubRouteRepository.existsByStartHubIdAndEndHubIdAndDeletedAtIsNull(startId, endId)) {
            throw new AppException(HubErrorCode.HUB_ROUTE_ALREADY_EXISTS);
        }

        Hub start = getHubOrThrow(startId);
        Hub end = getHubOrThrow(endId);

        var summary = calcIfNeeded(dto.getDistance(), dto.getDuration(), start, end);

        HubRoute route = HubRoute.builder().startHubId(startId).endHubId(endId)
            .distance(summary.distanceKm())  // km
            .duration(summary.durationMin()) // 분
            .build();

        return hubRouteRepository.save(route).getId();
    }

    @Override
    @Cacheable(value = CacheNames.HUB_ROUTE_BY_ID, key = "#routeId", sync = true)
    public GetHubRouteResponseDto getById(UUID routeId) {
        HubRoute r = hubRouteRepository.findByIdAndDeletedAtIsNull(routeId)
            .orElseThrow(() -> new AppException(HubErrorCode.HUB_ROUTE_NOT_FOUND));
        return GetHubRouteResponseDto.of(r);
    }

    @Override
    public Page<HubRoute> getPage(Pageable pageable) {
        pageable = PageableUtils.normalize(pageable);
        return hubRouteRepository.findAllByDeletedAtIsNull(pageable);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheNames.HUB_ROUTE_BY_ID, key = "#routeId")
    public void update(UUID routeId, UpdateHubRouteRequestDto request, UUID actorId) {
        HubRoute r = hubRouteRepository.findByIdAndDeletedAtIsNull(routeId)
            .orElseThrow(() -> new AppException(HubErrorCode.HUB_ROUTE_NOT_FOUND));

        var dto = request.getRoute();
        UUID startId = dto.getStartHubId();
        UUID endId = dto.getEndHubId();

        if (Objects.equals(startId, endId)) {
            throw new AppException(HubErrorCode.SAME_START_END_HUB);
        }

        Hub start = getHubOrThrow(startId);
        Hub end = getHubOrThrow(endId);

        var summary = calcIfNeeded(dto.getDistance(), dto.getDuration(), start, end);

        r.changeEndpoints(startId, endId);
        r.update(summary.distanceKm(), summary.durationMin());

    }

    @Override
    @Transactional
    @CacheEvict(value = CacheNames.HUB_ROUTE_BY_ID, key = "#routeId")
    public void delete(UUID routeId, UUID actorId) {
        HubRoute r = hubRouteRepository.findByIdAndDeletedAtIsNull(routeId)
            .orElseThrow(() -> new AppException(HubErrorCode.HUB_ROUTE_NOT_FOUND));
        r.delete(actorId);
        hubRouteRepository.save(r);
    }
}
