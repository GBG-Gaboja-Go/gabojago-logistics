package com.gbg.userservice.infrastructure.config.filter;

import com.gbg.userservice.domain.entity.UserRole;
import com.gbg.userservice.infrastructure.config.auth.CustomUser;
import com.gbg.userservice.infrastructure.config.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("Request URI: " + request.getRequestURI());
        if (path.equals("/v1/users/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("필터 진입 : " + request.getRequestURI());

//        // 이미 인증된 사용자인 경우 필터 통과
//        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (existingAuth != null && existingAuth.isAuthenticated()) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        String token = jwtTokenProvider.resolveToken(request);

        // 토큰이 없는 경우 (회원 가입, 로그인)
        if (token == null || token.isBlank()) {
            filterChain.doFilter(request,response);
            return;
        }

        // 토큰에서 사용자 정보 추출
        UUID id = jwtTokenProvider.getId(token);
        String username = jwtTokenProvider.getUsername(token);
        String role = jwtTokenProvider.getRole(token);
        UserRole userRole = UserRole.valueOf(role);

        // CustomUser 생성 (UserDetails 구현체)
        CustomUser customUser = new CustomUser(id, username, null, userRole);

        // 인증 객체 생성
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                customUser,
                null,
                customUser.getAuthorities()
            );

        authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );

        // SecurityContext에 등록 (권한 확인 가능)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
