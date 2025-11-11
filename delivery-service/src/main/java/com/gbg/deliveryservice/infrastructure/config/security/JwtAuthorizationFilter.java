//package com.gbg.deliveryservice.infrastructure.config.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.UUID;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//
//@RequiredArgsConstructor
//public class JwtAuthorizationFilter extends OncePerRequestFilter {
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//        FilterChain filterChain) throws ServletException, IOException {
//
//        System.out.println("필터 진입 : " + request.getRequestURI());
//
//        String token = jwtTokenProvider.resolveToken(request);
//
//        // 토큰에서 사용자 정보 추출
//        UUID id = jwtTokenProvider.getId(token);
//        String username = jwtTokenProvider.getUsername(token);
//        String role = jwtTokenProvider.getRole(token);
//        UserRole userRole = UserRole.valueOf(role);
//
//        // CustomUser 생성 (UserDetails 구현체)
//        CustomUser customUser = new CustomUser(id.toString(), userRole.getAuthority());
//
//        // 인증 객체 생성
//        UsernamePasswordAuthenticationToken authentication =
//            new UsernamePasswordAuthenticationToken(
//                customUser,
//                null,
//                customUser.getAuthorities()
//            );
//
//        authentication.setDetails(
//            new WebAuthenticationDetailsSource().buildDetails(request)
//        );
//
//        // SecurityContext에 등록 (권한 확인 가능)
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        filterChain.doFilter(request, response);
//    }
//}
