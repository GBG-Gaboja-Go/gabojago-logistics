package com.gbg.deliveryservice.application.service;

import com.gbg.deliveryservice.domain.entity.enums.DeliveryType;
import com.gbg.deliveryservice.infrastructure.config.security.CustomUser;
import com.gbg.deliveryservice.presentation.dto.request.CreateDeliveryManRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryManHubRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryManRequestDTO;
import com.gbg.deliveryservice.presentation.dto.response.CreateDeliveryManResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryManPageResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryManResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetMyDeliveryManResponseDTO;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface DeliveryManService {

    @Transactional
    CreateDeliveryManResponseDTO createDeliveryMan(CreateDeliveryManRequestDTO req,
        CustomUser customUser);

    @Transactional
    GetDeliveryManPageResponseDTO getDeliveryManPage(CustomUser customUser, Pageable pageable,
        DeliveryType type, UUID hub);

    @Transactional
    GetDeliveryManResponseDTO getDeliveryMan(CustomUser customUser, UUID id);

    @Transactional
    void deleteDeliveryMan(CustomUser customUser, UUID id);

    @Transactional
    GetMyDeliveryManResponseDTO getMyDeliveryManPage(CustomUser customUser);

    @Transactional
    void updateDeliveryManHub(CustomUser customUser, UpdateDeliveryManHubRequestDTO req, UUID id);

    @Transactional
    void updateDeliveryMan(CustomUser customUser, UpdateDeliveryManRequestDTO req, UUID id);
}
