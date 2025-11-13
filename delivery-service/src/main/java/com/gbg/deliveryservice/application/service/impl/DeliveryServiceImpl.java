package com.gbg.deliveryservice.application.service.impl;

import com.gabojago.exception.AppException;
import com.gbg.deliveryservice.application.service.DeliveryService;
import com.gbg.deliveryservice.domain.entity.Delivery;
import com.gbg.deliveryservice.domain.entity.DeliveryMan;
import com.gbg.deliveryservice.domain.entity.HubDelivery;
import com.gbg.deliveryservice.domain.entity.VendorDelivery;
import com.gbg.deliveryservice.domain.entity.enums.DeliverySearchType;
import com.gbg.deliveryservice.domain.entity.enums.DeliveryStatus;
import com.gbg.deliveryservice.domain.repository.DeliveryManRepository;
import com.gbg.deliveryservice.domain.repository.DeliveryRepository;
import com.gbg.deliveryservice.domain.repository.HubDeliveryRepository;
import com.gbg.deliveryservice.domain.repository.VendorDeliveryRepository;
import com.gbg.deliveryservice.infrastructure.client.HubFeignClient;
import com.gbg.deliveryservice.infrastructure.client.OrderFeignClient;
import com.gbg.deliveryservice.infrastructure.client.VendorFeignClient;
import com.gbg.deliveryservice.infrastructure.client.dto.VendorResponseDto.VendorDto;
import com.gbg.deliveryservice.infrastructure.config.security.CustomUser;
import com.gbg.deliveryservice.presentation.advice.DeliveryErrorCode;
import com.gbg.deliveryservice.presentation.advice.FeignClientError;
import com.gbg.deliveryservice.presentation.dto.request.CreateDeliveryRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryStatusRequestDTO;
import com.gbg.deliveryservice.presentation.dto.response.CreateDeliveryResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryPageResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetMyDeliveryResponseDTO;
import feign.FeignException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final HubDeliveryRepository hubDeliveryRepository;
    private final VendorDeliveryRepository vendorDeliveryRepository;
    private final OrderFeignClient orderFeignClient;
    private final HubFeignClient hubFeignClient;
    private final DeliveryManRepository deliveryManRepository;
    private final VendorFeignClient vendorFeignClient;

    @Override
    @Transactional
    public CreateDeliveryResponseDTO createDelivery(CreateDeliveryRequestDTO req, UUID userId) {

        try {
            orderFeignClient.getOrderDetail(req.delivery().getOrderId(), userId.toString(),
                "MASTER");
            if (deliveryRepository.existsByOrderIdAndDeletedAtIsNull(req.delivery().getOrderId())) {
                throw new AppException(DeliveryErrorCode.ALREADY_CREATE_DELIVERY_OF_ORDER);
            }

            hubFeignClient.getHub(req.delivery().getHubToId(), userId.toString(), "MASTER");
            hubFeignClient.getHub(req.delivery().getHubFromId(),
                userId.toString(), "MASTER");

            VendorDto vendor = vendorFeignClient.getVendorsById(
                req.delivery().getVendorFromId(),
                userId.toString(), "MASTER").getBody().getData();

            vendorFeignClient.getVendorsById(req.delivery().getVendorToId(), userId.toString(),
                "MASTER");

            Delivery delivery = Delivery.builder()
                .orderId(req.delivery().getOrderId())
                .status(DeliveryStatus.WAITING_FOR_HUB_DEPARTURE)
                .deliveryAddress(req.delivery().getDeliveryAddress())
                .build();

            Delivery saveDelivery = deliveryRepository.save(delivery);

            HubDelivery hubDelivery = HubDelivery.builder()
                .deliveryId(delivery.getId())
                .hubToId(req.delivery().getHubToId())
                .hubFromId(req.delivery().getHubFromId())
                .deliverymanId(assignNextDeliveryManHub().getId())
                .build();

            hubDeliveryRepository.save(hubDelivery);

            VendorDelivery vendorDelivery = VendorDelivery.builder()
                .deliveryId(delivery.getId())
                .vendorToId(req.delivery().getVendorToId())
                .vendorFromId(req.delivery().getVendorFromId())
                .deliverymanId(
                    assignNextDeliveryManVendor(
                        vendor.getId()
                    ).getId())

                .build();

            vendorDeliveryRepository.save(vendorDelivery);

            return CreateDeliveryResponseDTO.from(saveDelivery);

        } catch (FeignException e) {

            throw new AppException(
                FeignClientError.of(String.valueOf(e.status()), e.getMessage(),
                    HttpStatus.valueOf(e.status())));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public GetDeliveryPageResponseDTO getDeliveryPage(Pageable pageable, DeliveryStatus status,
        DeliverySearchType type, String keyword) {

        Page<Delivery> deliveries = deliveryRepository.deliveryPage(pageable, status);

        return GetDeliveryPageResponseDTO.from(deliveries);
    }

    @Transactional(readOnly = true)
    @Override
    public GetDeliveryResponseDTO getDelivery(UUID id, CustomUser customUser) {

        Delivery delivery = deliveryRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        HubDelivery hubDelivery = hubDeliveryRepository.findByDeliveryIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.HUB_DELIVERY_NOT_FOUND));

        if (customUser.role().equals("HUB_MANAGER")) {
            UUID hubId = Objects.requireNonNull(hubFeignClient.getHubManagerId(
                        UUID.fromString(customUser.userId()), customUser.userId(), customUser.role())
                    .getBody())
                .getData().getHub().getId();
            if (hubDelivery.getHubToId() != hubId || hubDelivery.getHubFromId() != hubId) {
                throw new AppException(DeliveryErrorCode.HUB_DELIVERY_FORBIDDEN);
            }
        }
        VendorDelivery vendorDelivery = vendorDeliveryRepository.findByDeliveryIdAndDeletedAtIsNull(
                id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.VENDOR_DELIVERY_NOT_FOUND));

        return GetDeliveryResponseDTO.from(delivery, hubDelivery, vendorDelivery);
    }

    @Transactional
    @Override
    public void updateDelivery(UpdateDeliveryRequestDTO req, CustomUser customUser, UUID id) {

        Delivery delivery = deliveryRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        if (customUser.role().equals("HUB_MANAGER")) {
            UUID hubId = Objects.requireNonNull(hubFeignClient.getHubManagerId(
                        UUID.fromString(customUser.userId()), customUser.userId(), customUser.role())
                    .getBody())
                .getData().getHub().getId();
            HubDelivery hubDelivery = hubDeliveryRepository.findByDeliveryIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(DeliveryErrorCode.HUB_DELIVERY_NOT_FOUND));
            if (hubDelivery.getHubToId() != hubId || hubDelivery.getHubFromId() != hubId) {
                throw new AppException(DeliveryErrorCode.HUB_DELIVERY_FORBIDDEN);
            }
        }
        delivery.update(
            (req.delivery().getDeliveryAddress() == null) ? delivery.getDeliveryAddress()
                : req.delivery().getDeliveryAddress(),
            (req.delivery().getEstimatedDistance() == 0) ? delivery.getEstimatedDistance()
                : req.delivery().getEstimatedDistance(),
            (req.delivery().getEstimatedTime() == null) ? delivery.getEstimatedTime()
                : req.delivery().getEstimatedTime(),
            (req.delivery().isUpdateStartTime()) ? req.delivery().getStartedTime()
                : delivery.getStartedTime(),
            (req.delivery().isUpdateCompletedTime()) ? req.delivery().getCompletedTime()
                : delivery.getCompletedTime()
        );

    }

    @Transactional
    @Override
    public void updateDeliveryStatus(UpdateDeliveryStatusRequestDTO req, CustomUser customUser,
        UUID id) {
        Delivery delivery = deliveryRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        if (customUser.role().equals("HUB_MANAGER")) {
            UUID hubId = Objects.requireNonNull(hubFeignClient.getHubManagerId(
                        UUID.fromString(customUser.userId()), customUser.userId(), customUser.role())
                    .getBody())
                .getData().getHub().getId();

            HubDelivery hubDelivery = hubDeliveryRepository.findByDeliveryIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(DeliveryErrorCode.HUB_DELIVERY_NOT_FOUND));
            if (hubDelivery.getHubToId() != hubId || hubDelivery.getHubFromId() != hubId) {
                throw new AppException(DeliveryErrorCode.HUB_DELIVERY_FORBIDDEN);
            }
        }
        delivery.updateStatus(req.delivery().getStatus());

    }

    @Override
    @Transactional
    public void startDelivery(UUID id, UUID userId) {
        Delivery delivery = deliveryRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        if (delivery.getStartedTime() != null) {
            throw new AppException(DeliveryErrorCode.DELIVERY_ALREADY_START);
        }

        delivery.startDelivery();

    }

    @Override
    @Transactional
    public void completedDelivery(UUID id, UUID userId) {
        Delivery delivery = deliveryRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        if (delivery.getCompletedTime() != null) {
            throw new AppException(DeliveryErrorCode.DELIVERY_ALREADY_COMPLETED);
        }

        delivery.completedDelivery();

    }

    @Transactional(readOnly = true)
    @Override
    public GetMyDeliveryResponseDTO getMyDeliveryPage(CustomUser customUser, Pageable pageable,
        DeliveryStatus status, DeliverySearchType type, String keyword) {

        // 딜리버리맨 만든뒤에 찾고 검색해서 나누기
        List<UUID> deliveryIdList = hubDeliveryRepository.findAllByDeliverymanIdAndDeletedAtIsNull(
            UUID.fromString(customUser.userId())).stream().map(HubDelivery::getDeliveryId).toList();
        // 업체배달담당자일때
//        List<UUID> deliveryIdList = vendorDeliveryRepository.findByDeliveryManIdAndDeletedAtIsNull(userId).stream().map(HubDelivery::getDeliveryId).toList();

        Page<Delivery> deliveries = deliveryRepository.deliveryMyPage(pageable, status,
            deliveryIdList);
        return GetMyDeliveryResponseDTO.from(deliveries);
    }

    @Transactional
    @Override
    public void deleteDelivery(UUID id, CustomUser customUser) {
        Delivery delivery = deliveryRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        if (customUser.role().equals("HUB_MANAGER")) {
            UUID hubId = Objects.requireNonNull(hubFeignClient.getHubManagerId(
                        UUID.fromString(customUser.userId()), customUser.userId(), customUser.role())
                    .getBody())
                .getData().getHub().getId();
            HubDelivery hubDelivery = hubDeliveryRepository.findByDeliveryIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(DeliveryErrorCode.HUB_DELIVERY_NOT_FOUND));
            if (hubDelivery.getHubToId() != hubId || hubDelivery.getHubFromId() != hubId) {
                throw new AppException(DeliveryErrorCode.HUB_DELIVERY_FORBIDDEN);
            }
        }

        delivery.delete(UUID.fromString(customUser.userId()));
    }

    @Override
    public GetMyDeliveryResponseDTO getHubSenderDeliveryPage(CustomUser customUser,
        Pageable pageable, DeliveryStatus status) {

        UUID hubId = Objects.requireNonNull(hubFeignClient.getHubManagerId(
                UUID.fromString(customUser.userId()), customUser.userId(), customUser.role()).getBody())
            .getData().getHub().getId();

        List<UUID> deliveryIdList = hubDeliveryRepository.findAllByHubFromIdAndDeletedAtIsNull(
            hubId).stream().map(HubDelivery::getDeliveryId).toList();

        Page<Delivery> deliveries = deliveryRepository.deliveryMyPage(pageable, status,
            deliveryIdList);

        return GetMyDeliveryResponseDTO.from(deliveries);
    }

    @Override
    public GetMyDeliveryResponseDTO getHubReceiverDeliveryPage(CustomUser customUser,
        Pageable pageable, DeliveryStatus status) {

        UUID hubId = Objects.requireNonNull(hubFeignClient.getHubManagerId(
                UUID.fromString(customUser.userId()), customUser.userId(), customUser.role()).getBody())
            .getData().getHub().getId();

        List<UUID> deliveryIdList = hubDeliveryRepository.findAllByDeletedAtIsNull(
        ).stream().map(HubDelivery::getDeliveryId).toList();

        Page<Delivery> deliveries = deliveryRepository.deliveryMyPage(pageable, status,
            deliveryIdList);

        return GetMyDeliveryResponseDTO.from(deliveries);
    }

    private DeliveryMan assignNextDeliveryManVendor(UUID hubId) {

        List<DeliveryMan> deliveryMen = deliveryManRepository.findAllByHubIdOrderBySequenceAsc(
            hubId);

        if (deliveryMen.isEmpty()) {
            throw new AppException(DeliveryErrorCode.DELIVERYMAN_NOT_FOUND);
        }

        VendorDelivery lastDelivery = vendorDeliveryRepository
            .findTopByVendorToIdOrderByCreatedAtDesc(hubId)
            .orElse(null);

        if (lastDelivery == null) {
            return deliveryMen.get(0);
        }

        UUID lastManId = lastDelivery.getDeliverymanId();
        DeliveryMan lastMan = deliveryManRepository.findByIdAndDeletedAtIsNull(lastManId)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.DELIVERYMAN_NOT_FOUND));

        if (lastMan == null) {
            return deliveryMen.get(0);
        }

        int lastSeq = lastMan.getSequence();

        return deliveryMen.stream()
            .filter(dm -> dm.getSequence() > lastSeq)
            .findFirst()
            .orElse(deliveryMen.get(0));
    }

    private DeliveryMan assignNextDeliveryManHub() {

        List<DeliveryMan> deliveryMen = deliveryManRepository.findAllByHubIdIsNullOrderBySequenceAsc();

        if (deliveryMen.isEmpty()) {
            throw new AppException(DeliveryErrorCode.DELIVERYMAN_NOT_FOUND);
        }

        HubDelivery lastDelivery = hubDeliveryRepository
            .findTopOrderByCreatedAtDesc()
            .orElse(null);

        if (lastDelivery == null) {
            return deliveryMen.get(0);
        }

        UUID lastManId = lastDelivery.getDeliverymanId();
        DeliveryMan lastMan = deliveryManRepository.findByIdAndDeletedAtIsNull(lastManId)
            .orElse(null);

        if (lastMan == null) {
            return deliveryMen.get(0);
        }

        int lastSeq = lastMan.getSequence();

        return deliveryMen.stream()
            .filter(dm -> dm.getSequence() > lastSeq)
            .findFirst()
            .orElse(deliveryMen.get(0));
    }

}
