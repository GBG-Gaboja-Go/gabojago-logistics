package com.gbg.deliveryservice.presentation.advice;

import com.gabojago.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AIErrorCode implements ErrorCode {
    GEMINI_NO_RESPONSE("AI000", "Gemini AI 응답 없음", HttpStatus.NO_CONTENT),
    GEMINI_API_ERROR("AI001", "Gemini API 호출 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    GEMINI_UNAUTHORIZED("AI002", "Gemini API 인증 실패", HttpStatus.UNAUTHORIZED),
    GEMINI_RATE_LIMIT("AI003", "Gemini API 요청 한도 초과", HttpStatus.TOO_MANY_REQUESTS),
    GEMINI_INVALID_REQUEST("AI004", "Gemini API 잘못된 요청", HttpStatus.BAD_REQUEST),
    GEMINI_TIMEOUT("AI005", "Gemini API 응답 시간 초과", HttpStatus.GATEWAY_TIMEOUT),
    GEMINI_PARSE_ERROR("AI006", "Gemini 응답 파싱 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    DEADLINE_CALCULATION_FAILED("AI007", "발송 시한 계산 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    AI_RESPONSE_PARSING_FAILED("AI008", "응답값 파싱 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    AI_HISTORY_NOT_FOUND("AI009", "해당 orderId의 AI 기록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;

    AIErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
