package com.gbg.userservice.infrastructure.exception;

import com.gabojago.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements ErrorCode {

    USER_UNAUTHORIZED("USER000", "사용자 인증에 실패하였습니다.", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("USER001", "해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_DUPLICATE_NAME("USER002", "이미 사용중인 ID 입니다.", HttpStatus.CONFLICT),
    USER_DUPLICATE_EMAIL("USER003", "이미 사용중인 Email 입니다.", HttpStatus.CONFLICT),
    USER_FORBIDDEN("USER004", "사용자 권한이 없습니다.", HttpStatus.FORBIDDEN),
    SELF_ROLE_UPDATE("USER005", "사용자 권한은 직접 변경할 수 없습니다.", HttpStatus.CONFLICT)
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    UserErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}