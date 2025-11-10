package com.gbg.hubservice.application.service.exception;

import com.gabojago.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HubErrorCode implements ErrorCode {

    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "허브를 찾을 수 없습니다.", "HUB_404"),
    HUB_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 허브입니다.", "HUB_409"),
    HUB_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "허브 접근 권한이 없습니다.", "HUB_403");

    private final HttpStatus status;
    private final String message;
    private final String code;
}
