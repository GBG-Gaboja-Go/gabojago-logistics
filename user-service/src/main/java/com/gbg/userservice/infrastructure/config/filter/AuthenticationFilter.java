package com.gbg.userservice.infrastructure.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabojago.exception.AppException;
import com.gabojago.exception.ErrorResponse;
import com.gbg.userservice.domain.entity.User;
import com.gbg.userservice.domain.entity.UserRole;
import com.gbg.userservice.domain.repository.UserRepository;
import com.gbg.userservice.infrastructure.config.auth.CustomUser;
import com.gbg.userservice.infrastructure.config.jwt.JwtTokenProvider;
import com.gbg.userservice.infrastructure.exception.UserErrorCode;
import com.gbg.userservice.presentation.dto.request.FormLoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String LOGIN_PROCESS_URL = "/v1/users/sign-in";

    public AuthenticationFilter(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper,  UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        setFilterProcessesUrl(LOGIN_PROCESS_URL);
    }

    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        try {
            FormLoginRequestDto loginRequest = objectMapper.readValue(request.getInputStream(),
                FormLoginRequestDto.class);

            User user = userRepository.findByUserName(loginRequest.username()).orElseThrow(
                () -> new AppException(UserErrorCode.USER_NOT_FOUND)
            );

            if (user.getDeletedAt() != null || user.getDeletedBy() != null) {
                throw new AppException(UserErrorCode.DELETED_USER);
            }

            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken
                .unauthenticated(loginRequest.username(), loginRequest.password());

            return this.getAuthenticationManager().authenticate(authRequest);

        } catch (Exception e) {
            System.out.println("로그인 실패 원인 : " + e.getClass().getSimpleName());
            System.out.println("메시지 : " + e.getMessage());
            throw new AppException(UserErrorCode.USER_UNAUTHORIZED);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult) {
        UUID id = ((CustomUser) authResult.getPrincipal()).getId();
        String username = ((CustomUser) authResult.getPrincipal()).getUsername();
        UserRole role = ((CustomUser) authResult.getPrincipal()).getRole();

        String token = jwtTokenProvider.createToken(id, username, role);
        String refreshToken = jwtTokenProvider.createRefreshToken(id);
        response.addHeader(AUTHORIZATION_HEADER, token);
        response.addHeader("Refresh-Token", refreshToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {

        response.setStatus(UserErrorCode.USER_UNAUTHORIZED.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse errorResponse = ErrorResponse.from(UserErrorCode.USER_UNAUTHORIZED);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
