package com.gbg.productservice.presentation.advice;

import com.gabojago.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProductErrorCode implements ErrorCode {
    PRODUCT_NOT_FOUND("PRODUCT001", "해당 상품 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_EXISTS("PRODUCT002", "이미 존재하는 상품입니다.", HttpStatus.CONFLICT),
    INVALID_PRODUCT_TYPE("PRODUCT003", "잘못된 상품 유형입니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK("PRODUCT004", "재고가 부족합니다.", HttpStatus.BAD_REQUEST),
    USER_FORBIDDEN("PRODUCT005", "권한이 없습니다.", HttpStatus.FORBIDDEN),
    VENDOR_NOT_FOUND("PRODUCT006", "해당 업체 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    HUB_NOT_FOUND("PRODUCT007", "해당 허브 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MISSING_TARGET("PRODUCT008", "업체 또는 허브 정보가 필요합니다.", HttpStatus.BAD_REQUEST),
    INVALID_TARGET_COMBINATION("PRODUCT009", "업체와 허브 정보를 동시에 보낼 수 없습니다.", HttpStatus.BAD_REQUEST);


    private final String code;
    private final String message;
    private final HttpStatus status;

    ProductErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
