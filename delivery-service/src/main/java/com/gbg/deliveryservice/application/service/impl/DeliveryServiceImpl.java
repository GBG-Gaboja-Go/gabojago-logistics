package com.gbg.deliveryservice.application.service.impl;

import com.gbg.deliveryservice.application.service.DeliveryService;
import com.gbg.deliveryservice.domain.entity.enums.DeliveryStatus;
import com.gbg.deliveryservice.presentation.dto.request.CreateDeliveryRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryStatusRequestDTO;
import com.gbg.deliveryservice.presentation.dto.response.CreateDeliveryResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryPageResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetMyDeliveryResponseDTO;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

//    private final DeliveryRepository deliveryRepository;

    @Override
    public CreateDeliveryResponseDTO createDelivery(CreateDeliveryRequestDTO req) {

        return null;
    }

    @Override
    public GetDeliveryPageResponseDTO getDeliveryPage(Pageable pageable, DeliveryStatus status,
        UUID sender, UUID receiver, UUID product) {
        return null;
    }

    @Override
    public GetDeliveryResponseDTO getDelivery(UUID id) {
        return null;
    }

    @Override
    public Void updateDelivery(UpdateDeliveryRequestDTO req, UUID id, UUID userId) {
        return null;
    }

    @Override
    public Void updateDeliveryStatus(UpdateDeliveryStatusRequestDTO req, UUID id, UUID userId) {
        return null;
    }

    @Override
    public GetMyDeliveryResponseDTO getMyDeliveryPage(UUID userId, Pageable pageable,
        DeliveryStatus status, UUID sender, UUID receiver, UUID product) {
        return null;
    }

    @Override
    public Void deleteDelivery(UUID id) {
        return null;
    }


}
