package com.gbg.deliveryservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.deliveryservice.application.service.DeliveryService;
import com.gbg.deliveryservice.presentation.dto.request.CreateDeliveryRequestDTO;
import com.gbg.deliveryservice.presentation.dto.response.CreateDeliveryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1/delivery")
public class InternalDeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    @Operation(summary = "배달 생성", description = "배달 생성하는 api 입니다.(예상 시간 입력 \"HH:mm:ss\" 이렇게 해야합니다.)")
    public ResponseEntity<BaseResponseDto<CreateDeliveryResponseDTO>> createDelivery(
        @RequestBody CreateDeliveryRequestDTO req
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponseDto.success("배달이 생성되었습니다.",
                deliveryService.createDelivery(req, UUID.randomUUID()), HttpStatus.CREATED));
    }

}
