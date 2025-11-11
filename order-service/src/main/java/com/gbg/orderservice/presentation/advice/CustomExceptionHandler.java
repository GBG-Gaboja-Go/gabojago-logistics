package com.gbg.orderservice.presentation.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabojago.exception.ErrorResponse;
import com.gabojago.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler extends GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Object> handleAuthorizeDeniedException(AuthorizationDeniedException ex) {
        ErrorResponse errorResponse = ErrorResponse.from(OrderErrorCode.USER_FORBIDDEN);

        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse);
    }
}
