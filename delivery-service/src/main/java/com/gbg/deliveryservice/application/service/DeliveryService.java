package com.gbg.deliveryservice.application.service;

import com.gbg.deliveryservice.domain.entity.enums.DeliveryStatus;
import com.gbg.deliveryservice.presentation.dto.request.CreateDeliveryRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryStatusRequestDTO;
import com.gbg.deliveryservice.presentation.dto.response.CreateDeliveryResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryPageResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetMyDeliveryResponseDTO;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface DeliveryService {

    CreateDeliveryResponseDTO createDelivery(CreateDeliveryRequestDTO req);

    GetDeliveryPageResponseDTO getDeliveryPage(Pageable pageable, DeliveryStatus status,
        UUID sender, UUID receiver, UUID product);

    GetDeliveryResponseDTO getDelivery(UUID id);

    Void updateDelivery(UpdateDeliveryRequestDTO req, UUID id, UUID userId);

    Void updateDeliveryStatus(UpdateDeliveryStatusRequestDTO req, UUID id, UUID userId);

    GetMyDeliveryResponseDTO getMyDeliveryPage(UUID userId, Pageable pageable,
        DeliveryStatus status,
        UUID sender, UUID receiver, UUID product);

    Void deleteDelivery(UUID id);
}
