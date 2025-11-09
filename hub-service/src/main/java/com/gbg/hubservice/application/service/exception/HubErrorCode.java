package com.gbg.hubservice.application.service.exception;

import org.springframework.http.HttpStatus;

public enum HubErrorCode {

    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "허브를 찾을 수 없습니다."),
    HUB_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 허브 입니다."),
    INVALID_HUB_REQUEST(HttpStatus.BAD_REQUEST, "허브 요청 정보가 올바르지 않습니다."),
    HUB_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "허브 삭제 처리 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

    HubErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
