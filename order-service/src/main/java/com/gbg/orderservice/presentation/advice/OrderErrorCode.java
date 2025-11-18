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
    ORDER_BAD_REQUEST("ORDER004", "잘못된 주문 요청입니다.", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_DELIVERING("ORDER005", "이미 배송 시작한 주문입니다.", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_DELIVERED("ORDER006", "이미 배송 완료한 주문입니다.", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_CANCELLED("ORDER007", "이미 취소된 주문입니다.", HttpStatus.BAD_REQUEST),
    USER_FORBIDDEN("ORDER008", "사용자 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ORDER_ONLY_RECEIVER_CAN_ORDER("ORDER009", "수령업체만 주문할 수 있습니다.", HttpStatus.FORBIDDEN),
    ORDER_SUPPLIER_VENDOR_ROLE_INVALID("ORDER010", "공급업체 역할을 가진 벤더 정보를 입력해야 합니다.",
        HttpStatus.BAD_REQUEST),
    ORDER_ACCESS_DENIED("ORDER011", "허브 관리자 역할이 아닙니다.", HttpStatus.FORBIDDEN),
    ORDER_HUB_MANAGER_NO_HUB("ORDER012", "담당 허브 관리자만 접근할 수 있습니다.", HttpStatus.FORBIDDEN),
    ORDER_ACCESS_DENIED_VENDOR("ORDER007", "담당 공급업체 관리자만 접근 가능합니다.", HttpStatus.FORBIDDEN);


    private final String code;
    private final String message;
    private final HttpStatus status;

    OrderErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
