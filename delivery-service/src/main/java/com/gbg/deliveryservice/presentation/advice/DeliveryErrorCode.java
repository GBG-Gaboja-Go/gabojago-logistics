package com.gbg.deliveryservice.presentation.advice;

import com.gabojago.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DeliveryErrorCode implements ErrorCode {
    DELIVERY_NOT_FOUND("DELIVERY000", "해당 배달을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    HUB_DELIVERY_NOT_FOUND("DELIVERY001", "해당 허브 배달 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    VENDOR_DELIVERY_NOT_FOUND("DELIVERY002", "해당 업체 배달 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DELIVERY_FORBIDDEN("DELIVERY003", "해당 배달에 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    DELIVERY_ALREADY_START("DELIVERY004", "이미 시작된 배달입니다.", HttpStatus.BAD_REQUEST),
    DELIVERY_ALREADY_COMPLETED("DELIVERY005", "이미 완료된 배달입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

    DeliveryErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
