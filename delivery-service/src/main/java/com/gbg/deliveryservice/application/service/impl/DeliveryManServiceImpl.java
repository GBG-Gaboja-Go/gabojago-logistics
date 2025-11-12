package com.gbg.deliveryservice.application.service.impl;

import com.gabojago.exception.AppException;
import com.gbg.deliveryservice.application.service.DeliveryManService;
import com.gbg.deliveryservice.domain.entity.DeliveryMan;
import com.gbg.deliveryservice.domain.entity.enums.DeliveryType;
import com.gbg.deliveryservice.domain.repository.DeliveryManRepository;
import com.gbg.deliveryservice.infrastructure.client.HubFeignClient;
import com.gbg.deliveryservice.infrastructure.client.dto.GetHubResponseDto;
import com.gbg.deliveryservice.infrastructure.config.security.CustomUser;
import com.gbg.deliveryservice.presentation.advice.DeliveryErrorCode;
import com.gbg.deliveryservice.presentation.advice.FeignClientError;
import com.gbg.deliveryservice.presentation.dto.request.CreateDeliveryManRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryManHubRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryManRequestDTO;
import com.gbg.deliveryservice.presentation.dto.response.CreateDeliveryManResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryManPageResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryManResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetMyDeliveryManResponseDTO;
import feign.FeignException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryManServiceImpl implements DeliveryManService {

    private final DeliveryManRepository deliveryManRepository;
    private final HubFeignClient hubFeignClient;

    @Override
    @Transactional
    public CreateDeliveryManResponseDTO createDeliveryMan(CreateDeliveryManRequestDTO req,
        CustomUser customUser) {

        try {
            GetHubResponseDto hub = hubFeignClient.getHub(
                    req.deliveryman().getHubId(), customUser.userId(), customUser.role()).getBody()
                .getData();

            if (customUser.role().equals("HUB_MANAGER")
                && hub.getHub().getUserId() != UUID.fromString(
                customUser.userId())) {
                throw new AppException(DeliveryErrorCode.HUB_DELIVERY_FORBIDDEN);
            }
            if (deliveryManRepository.existsByUserIdAndDeletedAtIsNull(req.deliveryman()
                .getUserId())) {

                throw new AppException(DeliveryErrorCode.DELIVERYMAN_EXIST);
            }

            if (deliveryManRepository.existsByHubIdAndSequenceAndDeletedAtIsNull(
                hub.getHub().getId(),
                req.deliveryman().getSequence())) {
                throw new AppException(DeliveryErrorCode.DELIVERYMAN_ALREADY_EXIST);
            }

            DeliveryMan deliveryMan = DeliveryMan.builder()
                .hubId(hub.getHub().getId())
                .id(req.deliveryman().getUserId())
                .type(hub.getHub().getId() != null ? DeliveryType.VENDOR : DeliveryType.HUB)
                .sequence(req.deliveryman().getSequence())
                .build();

            deliveryManRepository.save(deliveryMan);

            return CreateDeliveryManResponseDTO.from(deliveryMan.getId());

        } catch (FeignException e) {

            throw new AppException(
                FeignClientError.of(String.valueOf(e.status()), e.getMessage(),
                    HttpStatus.valueOf(e.status())));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GetDeliveryManPageResponseDTO getDeliveryManPage(CustomUser customUser,
        Pageable pageable, DeliveryType type, UUID hub) {

        if (customUser.role().equals("HUB_MANAGER")) {
            UUID hubId = hubFeignClient.getHubManagerId(UUID.fromString(customUser.userId()),
                    customUser.userId(), customUser.role())
                .getBody().getData().getHub().getId();
            Page<DeliveryMan> deliveryManPage = deliveryManRepository.findAllByHubIdAndDeletedAtIsNull(
                hubId, pageable);
            return GetDeliveryManPageResponseDTO.from(deliveryManPage);
        } else {
            Page<DeliveryMan> deliveryManPage = deliveryManRepository.findAllAndDeletedAtIsNull(
                pageable);
            return GetDeliveryManPageResponseDTO.from(deliveryManPage);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GetDeliveryManResponseDTO getDeliveryMan(CustomUser customUser, UUID id) {

        DeliveryMan deliveryMan = deliveryManRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.DELIVERYMAN_NOT_FOUND));

        if (customUser.role().equals("HUB_MANAGER")) {
            UUID hubId = hubFeignClient.getHubManagerId(UUID.fromString(customUser.userId()),
                    customUser.userId(), customUser.role())
                .getBody().getData().getHub().getId();

            if (deliveryMan.getHubId() != hubId) {
                throw new AppException(DeliveryErrorCode.HUB_DELIVERYMAN_FORBIDDEN);
            }

        }

        return GetDeliveryManResponseDTO.from(deliveryMan);
    }

    @Override
    @Transactional
    public void deleteDeliveryMan(CustomUser customUser, UUID id) {

        DeliveryMan deliveryMan = deliveryManRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.DELIVERYMAN_NOT_FOUND));

        if (customUser.role().equals("HUB_MANAGER")) {
            UUID hubId = hubFeignClient.getHubManagerId(UUID.fromString(customUser.userId()),
                    customUser.userId(), customUser.role())
                .getBody().getData().getHub().getId();

            if (deliveryMan.getHubId() != hubId) {
                throw new AppException(DeliveryErrorCode.HUB_DELIVERYMAN_FORBIDDEN);
            }

        }
        deliveryMan.delete(UUID.fromString(customUser.userId()));

    }

    @Override
    @Transactional(readOnly = true)
    public GetMyDeliveryManResponseDTO getMyDeliveryManPage(CustomUser customUser) {

        DeliveryMan deliveryMan = deliveryManRepository.findByIdAndDeletedAtIsNull(
                UUID.fromString(customUser.userId()))
            .orElseThrow(() -> new AppException(DeliveryErrorCode.DELIVERYMAN_NOT_FOUND));

        return GetMyDeliveryManResponseDTO.from(deliveryMan);
    }

    @Override
    @Transactional
    public void updateDeliveryManHub(CustomUser customUser, UpdateDeliveryManHubRequestDTO req,
        UUID id) {

        DeliveryMan deliveryMan = deliveryManRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.DELIVERYMAN_NOT_FOUND));

        GetHubResponseDto hub = hubFeignClient.getHub(req.deliveryMan().getHubId(),
                customUser.userId(), customUser.role()).getBody()
            .getData();

        deliveryMan.updateHub(hub.getHub().getId());
    }

    @Override
    @Transactional
    public void updateDeliveryMan(CustomUser customUser, UpdateDeliveryManRequestDTO req, UUID id) {

        DeliveryMan deliveryMan = deliveryManRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.DELIVERYMAN_NOT_FOUND));

        GetHubResponseDto hub = hubFeignClient.getHub(deliveryMan.getHubId(),
                customUser.userId(), customUser.role()).getBody()
            .getData();

        hubFeignClient.getHub(req.deliveryman().getHubId(), customUser.userId(), customUser.role());

        if (customUser.role().equals("HUB_MANAGER") && hub.getHub().getUserId() != UUID.fromString(
            customUser.userId())) {
            throw new AppException(DeliveryErrorCode.HUB_DELIVERY_FORBIDDEN);
        }

        if (req.deliveryman().getSequence() > 0
            && deliveryManRepository.existsByHubIdAndSequenceAndDeletedAtIsNull(
            hub.getHub().getId(),
            req.deliveryman().getSequence())) {
            throw new AppException(DeliveryErrorCode.DELIVERYMAN_ALREADY_EXIST);
        }

        deliveryMan.update(
            (req.deliveryman().getType() == null) ? deliveryMan.getType()
                : req.deliveryman().getType(),
            (req.deliveryman().getHubId() == null) ? deliveryMan.getHubId()
                : req.deliveryman().getHubId(),
            (req.deliveryman().getSequence() == 0) ? deliveryMan.getSequence()
                : req.deliveryman().getSequence()
        );

    }
}
