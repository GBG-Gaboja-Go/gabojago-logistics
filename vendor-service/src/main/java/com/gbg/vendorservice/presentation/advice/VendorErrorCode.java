package com.gbg.vendorservice.presentation.advice;

import com.gabojago.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum VendorErrorCode implements ErrorCode {
    VENDOR_NOT_FOUND("VENDOR001", "해당 업체 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    VENDOR_ALREADY_EXISTS("VENDOR002", "이미 존재하는 업체입니다.", HttpStatus.CONFLICT),
    INVALID_VENDOR_TYPE("VENDOR003", "잘못된 업체 유형입니다.", HttpStatus.BAD_REQUEST),
    USER_FORBIDDEN("VENDOR004", "권한이 없습니다.", HttpStatus.FORBIDDEN),
    HUB_NOT_FOUND("VENDOR005", "해당 허브 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);


    private final String code;
    private final String message;
    private final HttpStatus status;

    VendorErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
