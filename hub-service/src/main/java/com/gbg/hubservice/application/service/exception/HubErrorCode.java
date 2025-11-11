package com.gbg.hubservice.application.service.exception;

import com.gabojago.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HubErrorCode implements ErrorCode {

    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "허브를 찾을 수 없습니다.", "HUB_404"), HUB_ALREADY_EXISTS(
        HttpStatus.CONFLICT, "이미 존재하는 허브입니다.", "HUB_409"), HUB_PERMISSION_DENIED(
        HttpStatus.FORBIDDEN, "허브 접근 권한이 없습니다.", "HUB_403"),

    HUB_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "경로를 찾을 수 없습니다.",
        "ROUTE_404"), HUB_ROUTE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 경로입니다.", "ROUTE_409"),

    INVALID_COORDINATES(HttpStatus.BAD_REQUEST, "허브 좌표가 비어 있습니다.",
        "COMMON_400_1"), SAME_START_END_HUB(HttpStatus.BAD_REQUEST, "출발 허브와 도착 허브가 동일합니다.",
        "COMMON_400_2");

    private final HttpStatus status;
    private final String message;
    private final String code;
}
