package com.gbg.deliveryservice.application.service;

import com.gbg.deliveryservice.domain.entity.enums.DeliverySearchType;
import com.gbg.deliveryservice.domain.entity.enums.DeliveryStatus;
import com.gbg.deliveryservice.infrastructure.config.security.CustomUser;
import com.gbg.deliveryservice.presentation.dto.request.CreateDeliveryRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryStatusRequestDTO;
import com.gbg.deliveryservice.presentation.dto.response.CreateDeliveryResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryPageResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetMyDeliveryResponseDTO;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface DeliveryService {

    @Transactional
    CreateDeliveryResponseDTO createDelivery(CreateDeliveryRequestDTO req, UUID userId);

    @Transactional(readOnly = true)
    GetDeliveryPageResponseDTO getDeliveryPage(Pageable pageable, DeliveryStatus status,
        DeliverySearchType type, String keyword);

    @Transactional(readOnly = true)
    GetDeliveryResponseDTO getDelivery(UUID id, CustomUser customUser);

    @Transactional
    void updateDelivery(UpdateDeliveryRequestDTO req, CustomUser customUser, UUID userId);

    @Transactional
    void updateDeliveryStatus(UpdateDeliveryStatusRequestDTO req, CustomUser customUser,
        UUID userId);

    @Transactional
    void startDelivery(UUID id, UUID userId);

    @Transactional
    void completedDelivery(UUID id, UUID userId);

    @Transactional(readOnly = true)
    GetMyDeliveryResponseDTO getMyDeliveryPage(CustomUser customUser, Pageable pageable,
        DeliveryStatus status, DeliverySearchType type, String keyword);

    @Transactional
    void deleteDelivery(UUID id, CustomUser customUser);

    GetMyDeliveryResponseDTO getHubSenderDeliveryPage(CustomUser customUser, Pageable pageable,
        DeliveryStatus status);

    GetMyDeliveryResponseDTO getHubReceiverDeliveryPage(CustomUser customUser, Pageable pageable,
        DeliveryStatus status);
}
