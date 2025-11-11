package com.gbg.hubservice.application.service.impl;

import com.gabojago.exception.AppException;
import com.gbg.hubservice.application.service.exception.HubErrorCode;
import com.gbg.hubservice.domain.entity.Hub;
import com.gbg.hubservice.infrastructure.repository.HubJpaRepository;
import com.gbg.hubservice.presentation.dto.response.GetHubResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubTempService {

    private final HubJpaRepository hubRepository;

    public GetHubResponseDto getByUserId(UUID id) {
        Hub hub = hubRepository.findByUserId(id)
            .orElseThrow(() -> new AppException(HubErrorCode.HUB_NOT_FOUND));
        GetHubResponseDto dto = GetHubResponseDto.of(
            hub.getId(),
            hub.getName(),
            hub.getAddress(),
            hub.getLatitude(),
            hub.getLongitude()
        );
        return dto;
    }

}
