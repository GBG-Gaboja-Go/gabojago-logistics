package com.gbg.deliveryservice.application.service.impl;

import com.gabojago.exception.AppException;
import com.gbg.deliveryservice.application.service.DeliveryService;
import com.gbg.deliveryservice.domain.entity.Delivery;
import com.gbg.deliveryservice.domain.entity.HubDelivery;
import com.gbg.deliveryservice.domain.entity.VendorDelivery;
import com.gbg.deliveryservice.domain.entity.enums.DeliverySearchType;
import com.gbg.deliveryservice.domain.entity.enums.DeliveryStatus;
import com.gbg.deliveryservice.domain.repository.DeliveryRepository;
import com.gbg.deliveryservice.domain.repository.HubDeliveryRepository;
import com.gbg.deliveryservice.domain.repository.VendorDeliveryRepository;
import com.gbg.deliveryservice.presentation.advice.DeliveryErrorCode;
import com.gbg.deliveryservice.presentation.dto.request.CreateDeliveryRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryStatusRequestDTO;
import com.gbg.deliveryservice.presentation.dto.response.CreateDeliveryResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryPageResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetMyDeliveryResponseDTO;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final HubDeliveryRepository hubDeliveryRepository;
    private final VendorDeliveryRepository vendorDeliveryRepository;

    @Override
    @Transactional
    public CreateDeliveryResponseDTO createDelivery(CreateDeliveryRequestDTO req, UUID userId) {
        // orderId 검증하기(null 아니고 이미 배달이 생성된건지 확인
        // 출발, 도착 허브 검증하기(허브아이디는 유효한지 확인)
        // 공급업체 수령업체 검증하기(공급업체 수령업체 유효한지 확인)
        // hubRout 가져오기
        Delivery delivery = Delivery.builder()
            .orderId(req.delivery().getOrderId())
            .status(DeliveryStatus.WAITING_FOR_HUB_DEPARTURE)
            .estimatedDistance(req.delivery().getEstimatedDistance())
            .estimatedTime(req.delivery().getEstimatedTime())
            .deliveryAddress(req.delivery().getDeliveryAddress())
            .build();

        Delivery saveDelivery = deliveryRepository.save(delivery);

        HubDelivery hubDelivery = HubDelivery.builder()
            .deliveryId(delivery.getId())
            .hubToId(req.delivery().getHubToId())
            .hubFromId(req.delivery().getHubFromId())
            .deliverymanId(UUID.randomUUID()) // 딜리버리맨 만들어지면 집어넣기 순서 고려해서 (알고리즘에 따라 여러개 생길수도 있음)
            .build();

        hubDeliveryRepository.save(hubDelivery);

        VendorDelivery vendorDelivery = VendorDelivery.builder()
            .deliveryId(delivery.getId())
            .userFromId(req.delivery().getUserFromId())
            .userToId(req.delivery().getUserToId())
            .deliverymanId(UUID.randomUUID())   // 배달 담당자 생기면 도착허브 소속 딜리버리맨 조회해서 집어넣기 순서 고려해서
            .build();

        vendorDeliveryRepository.save(vendorDelivery);

        return CreateDeliveryResponseDTO.from(saveDelivery);
    }

    @Override
    @Transactional(readOnly = true)
    public GetDeliveryPageResponseDTO getDeliveryPage(Pageable pageable, DeliveryStatus status,
        DeliverySearchType type, String keyword) {
        Page<Delivery> deliveries = deliveryRepository.deliveryPage(pageable, status);

        return GetDeliveryPageResponseDTO.from(deliveries);
    }

    @Override
    @Transactional(readOnly = true)
    public GetDeliveryResponseDTO getDelivery(UUID id) {

        Delivery delivery = deliveryRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        HubDelivery hubDelivery = hubDeliveryRepository.findByDeliveryIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.HUB_DELIVERY_NOT_FOUND));

        VendorDelivery vendorDelivery = vendorDeliveryRepository.findByDeliveryIdAndDeletedAtIsNull(
                id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.VENDOR_DELIVERY_NOT_FOUND));

        return GetDeliveryResponseDTO.from(delivery, hubDelivery, vendorDelivery);
    }

    @Override
    @Transactional
    public void updateDelivery(UpdateDeliveryRequestDTO req, UUID id, UUID userId) {

        Delivery delivery = deliveryRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

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

    @Override
    @Transactional
    public void updateDeliveryStatus(UpdateDeliveryStatusRequestDTO req, UUID id, UUID userId) {
        Delivery delivery = deliveryRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

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

    @Override
    @Transactional(readOnly = true)
    public GetMyDeliveryResponseDTO getMyDeliveryPage(UUID userId, Pageable pageable,
        DeliveryStatus status, DeliverySearchType type, String keyword) {

        // 딜리버리맨 만든뒤에 찾고 검색해서 나누기
        List<UUID> deliveryIdList = hubDeliveryRepository.findAllByDeliverymanIdAndDeletedAtIsNull(
            userId).stream().map(HubDelivery::getDeliveryId).toList();
        // 업체일때
//        List<UUID> deliveryIdList = vendorDeliveryRepository.findByDeliveryManIdAndDeletedAtIsNull(userId).stream().map(HubDelivery::getDeliveryId).toList();

        Page<Delivery> deliveries = deliveryRepository.deliveryMyPage(pageable, status,
            deliveryIdList);
        return GetMyDeliveryResponseDTO.from(deliveries);
    }

    @Override
    @Transactional
    public Void deleteDelivery(UUID id, UUID userId) {
        Delivery delivery = deliveryRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new AppException(DeliveryErrorCode.DELIVERY_NOT_FOUND));
        delivery.delete(userId);
        return null;
    }

}
