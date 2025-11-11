package com.gbg.slackservice.infrastructure.exception;

import com.gabojago.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SlackError implements ErrorCode {

    USER_UNAUTHORIZED("USER000", "사용자 인증에 실패하였습니다.", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("USER001", "해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_FORBIDDEN("USER004", "사용자 권한이 없습니다.", HttpStatus.FORBIDDEN),
    SLACK_USER_NOT_FOUND("SLACK000", "슬랙 채널에 등록되지 않은 사용자 입니다.", HttpStatus.NOT_FOUND),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    SlackError(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
