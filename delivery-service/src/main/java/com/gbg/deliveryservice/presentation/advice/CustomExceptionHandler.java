package com.gbg.deliveryservice.presentation.advice;

import com.gabojago.exception.ErrorResponse;
import com.gabojago.exception.GlobalExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Object> handleAuthorizeDeniedException(AuthorizationDeniedException ex) {
        ErrorResponse errorResponse = ErrorResponse.from(DeliveryErrorCode.DELIVERY_FORBIDDEN);

        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse);
    }
}
