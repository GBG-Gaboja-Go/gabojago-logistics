package com.gbg.deliveryservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.deliveryservice.application.service.DeliveryManService;
import com.gbg.deliveryservice.domain.entity.enums.DeliveryType;
import com.gbg.deliveryservice.infrastructure.config.security.CustomUser;
import com.gbg.deliveryservice.presentation.dto.request.CreateDeliveryManRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryManHubRequestDTO;
import com.gbg.deliveryservice.presentation.dto.request.UpdateDeliveryManRequestDTO;
import com.gbg.deliveryservice.presentation.dto.response.CreateDeliveryManResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryManPageResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetDeliveryManResponseDTO;
import com.gbg.deliveryservice.presentation.dto.response.GetMyDeliveryManResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/v1/delivery/delivery-mans")
public class DeliveryManController {

    private final DeliveryManService deliveryManService;

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    @Operation(summary = "배달 담당자 생성", description = "배달 담당자를 지정하는 api 입니다.")
    public ResponseEntity<BaseResponseDto<CreateDeliveryManResponseDTO>> createDeliveryMan(
        @RequestBody CreateDeliveryManRequestDTO req,
        @AuthenticationPrincipal CustomUser customUser
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponseDto.success("배달 담당자가 생성되었습니다.",
                deliveryManService.createDeliveryMan(req, customUser), HttpStatus.CREATED));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    @Operation(summary = "배달 담당자 조회 및 검색", description = "배달 담당자 목록을 조회 및 검색하는 api 입니다.")
    public ResponseEntity<BaseResponseDto<GetDeliveryManPageResponseDTO>> getDeliveryManPage(
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable,
        @RequestParam(required = false) DeliveryType type,
        @RequestParam(required = false) UUID hub,
        @AuthenticationPrincipal CustomUser customUser
    ) {

        return ResponseEntity.status(HttpStatus.OK).body(
            BaseResponseDto.success("배달 담당자 목록입니다.",
                deliveryManService.getDeliveryManPage(customUser, pageable, type, hub),
                HttpStatus.OK));
    }


    @GetMapping("/{deliveryManId}")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    @Operation(summary = "배달 담당자 조회", description = "배달 담당자를 조회하는 api 입니다.")
    public ResponseEntity<BaseResponseDto<GetDeliveryManResponseDTO>> getDeliveryMan(
        @PathVariable("deliveryManId") UUID id,
        @AuthenticationPrincipal CustomUser customUser
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            BaseResponseDto.success("배달 담당자 단건 조회",
                deliveryManService.getDeliveryMan(customUser, id),
                HttpStatus.OK));
    }

    @PutMapping("/{deliveryManId}")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    @Operation(summary = "배달 담당자 수정", description = "배달 담당자를 수정하는 api 입니다.")
    public ResponseEntity<BaseResponseDto<Void>> updateDeliveryMan(
        @PathVariable("deliveryManId") UUID id,
        @RequestBody UpdateDeliveryManRequestDTO req,
        @AuthenticationPrincipal CustomUser customUser
    ) {
        deliveryManService.updateDeliveryMan(customUser, req, id);

        return ResponseEntity.status(HttpStatus.OK)
            .body(BaseResponseDto.success("배달담당자 정보가 변경되었습니다.", HttpStatus.OK));
    }

    @PatchMapping("/{deliveryManId}/hub")
    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary = "배달 담당자 허브 변경", description = "배달 담당자 담당허브를 변경하는 api 입니다.")
    public ResponseEntity<BaseResponseDto<Void>> updateDeliveryManHub(
        @PathVariable("deliveryManId") UUID id,
        @RequestBody UpdateDeliveryManHubRequestDTO req,
        @AuthenticationPrincipal CustomUser customUser
    ) {
        deliveryManService.updateDeliveryManHub(customUser, req, id);

        return ResponseEntity.status(HttpStatus.OK)
            .body(BaseResponseDto.success("담당 허브가 변경되었습니다.", HttpStatus.OK));
    }

    @GetMapping("/my")
    @Operation(summary = "배달 담당자 본인 정보 조회", description = "배달 담당자 본인을 조회하는 api 입니다.")
    public ResponseEntity<BaseResponseDto<GetMyDeliveryManResponseDTO>> getMyDeliveryManPage(
        @AuthenticationPrincipal CustomUser customUser
    ) {

        return ResponseEntity.status(HttpStatus.OK).body(
            BaseResponseDto.success("배달담당자 본인 정보입니다.",
                deliveryManService.getMyDeliveryManPage(customUser), HttpStatus.OK));
    }

    @DeleteMapping("/{deliveryManId}")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    @Operation(summary = "배달 담당자 삭제", description = "배달 담당자를 삭제하는 api 입니다.")
    public ResponseEntity<BaseResponseDto<Void>> deleteDeliveryMan(
        @PathVariable("deliveryManId") UUID id,
        @AuthenticationPrincipal CustomUser customUser
    ) {
        deliveryManService.deleteDeliveryMan(customUser, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(BaseResponseDto.success("삭제되었습니다.", HttpStatus.NO_CONTENT));
    }
}



