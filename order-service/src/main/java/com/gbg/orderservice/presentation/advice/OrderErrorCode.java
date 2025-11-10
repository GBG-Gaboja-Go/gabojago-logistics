package com.gbg.orderservice.presentation.advice;

import com.gabojago.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum OrderErrorCode implements ErrorCode {

    ORDER_NOT_FOUND("ORDER000", "해당 주문을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ORDER_FORBIDDEN("ORDER001", "해당 주문에 접근할 수 없습니다.", HttpStatus.FORBIDDEN),
    ORDER_PRODUCT_NOT_FOUND("ORDER002", "주문 상품 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    ORDER_PRODUCT_OUT_OF_STOCK("ORDER003", "상품 재고가 부족합니다.", HttpStatus.BAD_REQUEST),
    ORDER_INVALID_QUANTITY("ORDER004", "주문 수량이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    ORDER_BAD_REQUEST("ORDER004", "잘못된 주문 요청입니다.", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_DELIVERING("ORDER005", "이미 배송 시작한 주문입니다.", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_DELIVERED("ORDER006", "이미 배송 완료한 주문입니다.", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_CANCELLED("ORDER007", "이미 취소된 주문입니다.", HttpStatus.BAD_REQUEST);


    private final String code;
    private final String message;
    private final HttpStatus status;

    OrderErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
