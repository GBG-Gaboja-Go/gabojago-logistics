package com.gbg.hubservice.application.service.impl;

import com.gabojago.exception.AppException;
import com.gbg.hubservice.application.service.HubService;
import com.gbg.hubservice.application.service.exception.HubErrorCode;
import com.gbg.hubservice.domain.entity.Hub;
import com.gbg.hubservice.infrastructure.repository.HubJpaRepository;
import com.gbg.hubservice.presentation.dto.request.CreateHubRequestDto;
import com.gbg.hubservice.presentation.dto.request.UpdateHubRequestDto;
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

    private final HubJpaRepository hubRepository;

    @Override
    @Transactional
    public UUID create(CreateHubRequestDto request, UUID userId) {
        CreateHubRequestDto.HubDto hubDto = request.getHub();

        if (hubRepository.existsByName(hubDto.getName())) {
            throw new AppException(HubErrorCode.HUB_ALREADY_EXISTS);
        }

        Hub hub = Hub.builder()
            .name(hubDto.getName())
            .address(hubDto.getAddress())
            .latitude(hubDto.getLatitude())
            .longitude(hubDto.getLongitude())
            .userId(userId)
            .build();

        Hub savedHub = hubRepository.save(hub);
        return savedHub.getId();
    }

    @Override
    public Hub getById(UUID id) {
        return hubRepository.findById(id)
            .orElseThrow(() -> new AppException(HubErrorCode.HUB_NOT_FOUND));
    }

    @Override
    public Page<Hub> getPage(Pageable pageable) {
        return hubRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void update(UUID hubId, UpdateHubRequestDto request) {
        Hub hub = getById(hubId);
        UpdateHubRequestDto.HubDto hubDto = request.getHub();

        hub.update(
            hubDto.getName(),
            hubDto.getAddress(),
            hubDto.getLatitude(),
            hubDto.getLongitude()
        );
    }

    @Override
    @Transactional
    public void delete(UUID hubId, UUID userId) {
        Hub hub = getById(hubId);
        hub.delete(userId);
        hubRepository.save(hub);
    }
}
