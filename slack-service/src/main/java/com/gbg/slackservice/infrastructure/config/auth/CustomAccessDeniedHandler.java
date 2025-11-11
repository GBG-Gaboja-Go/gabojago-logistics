package com.gbg.slackservice.infrastructure.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabojago.exception.AppException;
import com.gabojago.exception.ErrorResponse;

import com.gbg.slackservice.infrastructure.exception.SlackError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 인가 실패 시 -
 * 사용자가 인증은 됐지만 해당 리소스 접근 권한이 없을 때 (403 Forbidden) 발생 하는 상황을
 * JSON 형식으로 응답하도록 하는 클래스
 */
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        AppException exception = new AppException(SlackError.USER_FORBIDDEN);

        response.setStatus(exception.getErrorCode().getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String body = objectMapper.writeValueAsString(
            ErrorResponse.from(exception)
        );

        response.getWriter().write(body);
    }
}
