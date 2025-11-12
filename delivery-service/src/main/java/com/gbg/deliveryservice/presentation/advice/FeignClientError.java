package com.gbg.deliveryservice.presentation.advice;

import com.gabojago.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class FeignClientError implements ErrorCode {

    private String code;
    private String message;
    private HttpStatus status;

    public static FeignClientError of(String code, String message, HttpStatus status) {
        return new FeignClientError(code, message, status);
    }

}
