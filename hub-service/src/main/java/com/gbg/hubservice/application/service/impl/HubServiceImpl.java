package com.gbg.hubservice.application.service.impl;

import com.gabojago.exception.AppException;
import com.gabojago.util.PageableUtils;
import com.gbg.hubservice.application.service.HubService;
import com.gbg.hubservice.application.service.exception.HubErrorCode;
import com.gbg.hubservice.domain.entity.Hub;
import com.gbg.hubservice.domain.repository.HubRepository;
import com.gbg.hubservice.infrastructure.kakao.KakaoLocalClient;
import com.gbg.hubservice.presentation.dto.request.CreateHubRequestDto;
import com.gbg.hubservice.presentation.dto.request.UpdateHubRequestDto;
import com.gbg.hubservice.presentation.dto.response.GetHubResponseDto;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubServiceImpl implements HubService {

    private final HubRepository hubRepository;
    private final KakaoLocalClient kakaoLocalClient; // ✅ 지오코딩 클라이언트

    @Override
    @Transactional
    public UUID create(CreateHubRequestDto request, UUID userId) {
        CreateHubRequestDto.HubDto hubDto = request.getHub();

        if (hubRepository.existsByName(hubDto.getName())) {
            throw new AppException(HubErrorCode.HUB_ALREADY_EXISTS);
        }

        // 1) 좌표 결정: 요청에 좌표 없으면 카카오 지오코딩으로 보정
        BigDecimal lat = hubDto.getLatitude();
        BigDecimal lon = hubDto.getLongitude();

        if (lat == null || lon == null) {
            KakaoLocalClient.GeoPoint p = kakaoLocalClient.geocodeAddress(hubDto.getAddress());
            lat = BigDecimal.valueOf(p.lat());
            lon = BigDecimal.valueOf(p.lon());
        }

        // 2) 저장
        Hub hub = Hub.builder()
            .name(hubDto.getName())
            .address(hubDto.getAddress())
            .latitude(lat)
            .longitude(lon)
            .userId(userId)
            .build();

        return hubRepository.save(hub).getId();
    }

    @Override
    public Hub getById(UUID id) {
        return hubRepository.findById(id)
            .orElseThrow(() -> new AppException(HubErrorCode.HUB_NOT_FOUND));
    }

    @Override
    public Page<Hub> getPage(Pageable pageable) {
        pageable = PageableUtils.normalize(pageable);
        return hubRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void update(UUID hubId, UpdateHubRequestDto request) {
        Hub hub = getById(hubId);
        UpdateHubRequestDto.HubDto dto = request.getHub();

        String newName = dto.getName();
        String newAddress = dto.getAddress();
        BigDecimal newLat = dto.getLatitude();
        BigDecimal newLon = dto.getLongitude();

        boolean addressChanged =
            newAddress != null && !Objects.equals(hub.getAddress(), newAddress);
        boolean coordsMissing = (newLat == null || newLon == null);

        // 주소가 바뀌었거나 좌표가 비어 있으면 지오코딩으로 다시 계산
        if ((addressChanged && (newLat == null && newLon == null)) || coordsMissing) {
            String addressForGeocoding = (newAddress != null) ? newAddress : hub.getAddress();
            KakaoLocalClient.GeoPoint p = kakaoLocalClient.geocodeAddress(addressForGeocoding);
            newLat = BigDecimal.valueOf(p.lat());
            newLon = BigDecimal.valueOf(p.lon());
        }

        // 널이면 기존값 유지
        String finalName = (newName != null) ? newName : hub.getName();
        String finalAddress = (newAddress != null) ? newAddress : hub.getAddress();
        BigDecimal finalLat = (newLat != null) ? newLat : hub.getLatitude();
        BigDecimal finalLon = (newLon != null) ? newLon : hub.getLongitude();

        hub.update(finalName, finalAddress, finalLat, finalLon);
    }

    @Override
    @Transactional
    public void delete(UUID hubId, UUID userId) {
        Hub hub = getById(hubId);
        hub.delete(userId);
        hubRepository.save(hub);
    }

    @Override
    public GetHubResponseDto getByUserId(UUID id) {
        Hub hub = hubRepository.findByUserId(id)
            .orElseThrow(() -> new AppException(HubErrorCode.HUB_NOT_FOUND));
        return GetHubResponseDto.of(
            hub.getId(),
            hub.getName(),
            hub.getAddress(),
            hub.getLatitude(),
            hub.getLongitude()
        );
    }
}
