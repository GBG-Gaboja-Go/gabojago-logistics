package com.gbg.hubservice.application.service;

import com.gbg.hubservice.domain.entity.Hub;
import com.gbg.hubservice.presentation.dto.request.CreateHubRequestDto;
import com.gbg.hubservice.presentation.dto.request.UpdateHubRequestDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubService {

    UUID create(CreateHubRequestDto request, UUID userId);

    Hub getById(UUID hubId);

    Page<Hub> getPage(Pageable pageable);

    void update(UUID hubId, UpdateHubRequestDto request);

    void delete(UUID hubId, UUID actorId);
}
