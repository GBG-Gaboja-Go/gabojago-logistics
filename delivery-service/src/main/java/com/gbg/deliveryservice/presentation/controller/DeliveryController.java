package com.gbg.deliveryservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.deliveryservice.application.service.DeliveryService;
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
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary = "배달 생성", description = "배달 생성하는 api 입니다.")
    public ResponseEntity<BaseResponseDto<CreateDeliveryResponseDTO>> createDelivery(
        @RequestBody @Valid CreateDeliveryRequestDTO req,
        @AuthenticationPrincipal CustomUser customUser
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponseDto.success("배달이 생성되었습니다.",
                deliveryService.createDelivery(req, UUID.fromString(customUser.userId())),
                HttpStatus.CREATED));
    }

    @GetMapping
    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary = "배달 조회 및 검색", description = "배달 목록을 조회 및 검색하는 api 입니다.")
    public ResponseEntity<BaseResponseDto<GetDeliveryPageResponseDTO>> getDeliveryPage(
        @AuthenticationPrincipal CustomUser customUser,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable,
        @RequestParam(required = false) DeliveryStatus status,
        @RequestParam(required = false) DeliverySearchType type,
        @RequestParam(required = false) String keyword
    ) {

        return ResponseEntity.status(HttpStatus.OK).body(
            BaseResponseDto.success("배달 목록입니다.",
                deliveryService.getDeliveryPage(pageable, status, type, keyword),
                HttpStatus.OK));
    }


    @GetMapping("/{deliveryId}")
    @Operation(summary = "배달 단건 조회", description = "배달 조회하는 api 입니다.")
    public ResponseEntity<BaseResponseDto<GetDeliveryResponseDTO>> getDelivery(
        @PathVariable("deliveryId") UUID id,
        @AuthenticationPrincipal CustomUser customUser
    ) {

        return ResponseEntity.ok(
            BaseResponseDto.success("배달이 조회되었습니다.", deliveryService.getDelivery(id, customUser),
                HttpStatus.OK));
    }

    @PutMapping("/{deliveryId}")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    @Operation(summary = "배달 수정", description = "배달 수정하는 api 입니다.(소요시간 입력 \"HH:mm:ss\" 이렇게 해야합니다.)")
    public ResponseEntity<BaseResponseDto<Void>> updateDelivery(
        @PathVariable("deliveryId") UUID id,
        @RequestBody @Valid UpdateDeliveryRequestDTO req,
        @AuthenticationPrincipal CustomUser customUser
    ) {
        deliveryService.updateDelivery(req, customUser, id);

        return ResponseEntity.ok(BaseResponseDto.success("배달정보가 변경되었습니다.", HttpStatus.OK));
    }

    @PatchMapping("/{deliveryId}/status")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    @Operation(summary = "배달 상태 변경", description = "배달 상태를 변경하는 api 입니다.")
    public ResponseEntity<BaseResponseDto<Void>> updateDeliveryStatus(
        @PathVariable("deliveryId") UUID id,
        @RequestBody UpdateDeliveryStatusRequestDTO req,
        @AuthenticationPrincipal CustomUser customUser
    ) {
        deliveryService.updateDeliveryStatus(req, customUser, id);

        return ResponseEntity.ok(BaseResponseDto.success("상태가 변경되었습니다.", HttpStatus.OK));
    }


    @PatchMapping("/{deliveryId}/start")
    @Operation(summary = "배달 시작", description = "배달을 시작하는 api 입니다.")
    @PreAuthorize("hasRole('DELIVERY_MANAGER')")
    public ResponseEntity<BaseResponseDto<Void>> startDelivery(
        @PathVariable("deliveryId") UUID id,
        @AuthenticationPrincipal CustomUser customUser
    ) {
        deliveryService.startDelivery(id, UUID.fromString(customUser.userId()));

        return ResponseEntity.ok(BaseResponseDto.success("배달이 시작되었습니다.", HttpStatus.OK));
    }


    @PatchMapping("/{deliveryId}/completed")
    @PreAuthorize("hasRole('DELIVERY_MANAGER')")
    @Operation(summary = "배달 종료", description = "배달을 종료하는 api 입니다.")
    public ResponseEntity<BaseResponseDto<Void>> completedDelivery(
        @PathVariable("deliveryId") UUID id,
        @AuthenticationPrincipal CustomUser customUser
    ) {
        deliveryService.completedDelivery(id, UUID.fromString(customUser.userId()));

        return ResponseEntity.ok(BaseResponseDto.success("상태가 변경되었습니다.", HttpStatus.OK));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('DELIVERY_MANAGER')")
    @Operation(summary = "배달 담당자 본인 목록 조회 및 검색", description = "배달 담당자 본인의 목록을 조회 및 검색하는 api 입니다.")
    public ResponseEntity<BaseResponseDto<GetMyDeliveryResponseDTO>> getMyDeliveryPage(
        @AuthenticationPrincipal CustomUser customUser,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable,
        @RequestParam(required = false) DeliveryStatus status,
        @RequestParam(required = false) DeliverySearchType type,
        @RequestParam(required = false) String keyword
    ) {

        return ResponseEntity.ok(
            BaseResponseDto.success("배달담당자 본인의 배달 목록입니다.",
                deliveryService.getMyDeliveryPage(customUser, pageable,
                    status, type,
                    keyword), HttpStatus.OK));
    }


    @GetMapping("/hub-sender")
    @PreAuthorize("hasRole('HUB_MANAGER')")
    @Operation(summary = "허브 발신 및 발신예정 목록 조회 및 검색", description = "허브매니저가 허브의 배달 목록을 조회 및 검색하는 api 입니다.")
    public ResponseEntity<BaseResponseDto<GetMyDeliveryResponseDTO>> getHubSenderDeliveryPage(
        @AuthenticationPrincipal CustomUser customUser,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable,
        @RequestParam(required = false) DeliveryStatus status
    ) {

        return ResponseEntity.ok(
            BaseResponseDto.success("허브 발신 배달 목록입니다.",
                deliveryService.getHubSenderDeliveryPage(customUser, pageable, status),
                HttpStatus.OK));
    }

    @GetMapping("/hub-receiver")
    @PreAuthorize("hasRole('HUB_MANAGER')")
    @Operation(summary = "허브 수신 및 수신예정 목록 조회 및 검색", description = "허브매니저가 허브의 배달 목록을 조회 및 검색하는 api 입니다.")
    public ResponseEntity<BaseResponseDto<GetMyDeliveryResponseDTO>> getHubReceiverDeliveryPage(
        @AuthenticationPrincipal CustomUser customUser,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable,
        @RequestParam(required = false) DeliveryStatus status
    ) {

        return ResponseEntity.ok(
            BaseResponseDto.success("허브 수신 배달 목록입니다.",
                deliveryService.getHubReceiverDeliveryPage(customUser, pageable, status),
                HttpStatus.OK));
    }

    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    @DeleteMapping("/{deliveryId}")
    @Operation(summary = "배달 삭제", description = "배달 삭제하는 api 입니다.")
    public ResponseEntity<BaseResponseDto<Void>> deleteDelivery(
        @PathVariable("deliveryId") UUID id,
        @AuthenticationPrincipal CustomUser customUser
    ) {

        deliveryService.deleteDelivery(id, customUser);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(BaseResponseDto.success("삭제되었습니다.", HttpStatus.NO_CONTENT));
    }
}



