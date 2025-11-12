package com.gbg.deliveryservice.presentation.advice;

import com.gabojago.exception.ErrorCode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class FeignClientError implements ErrorCode {

    private String code;
    private String message;
    private HttpStatus status;

    public static FeignClientError of(String code, String messages, HttpStatus status) {

        String message = extractValue(messages, "\"message\"\\s*:\\s*\"([^\"]+)\"");

        return new FeignClientError(code, message, status);
    }

    private static String extractValue(String messages, String s) {

        Pattern pattern = Pattern.compile(s);
        Matcher matcher = pattern.matcher(messages);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
