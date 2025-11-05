package com.gbg.userservice.infrastructure.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabojago.exception.ErrorResponse;
import com.gbg.userservice.infrastructure.exception.UserErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 인가 실패 시 -
 * 사용자가 인증은 됐지만 해당 리소스 접근 권한이 없을 때 (403 Forbidden) 발생 하는 상황을
 * JSON 형식으로 응답하도록 하는 클래스
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = ErrorResponse.from(UserErrorCode.USER_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}