package com.gbg.vendorservice.infrastructure.config.filter;

import com.gbg.vendorservice.infrastructure.config.auth.CustomUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class HeaderAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String userId = request.getHeader("X-Auth-Id");
        String role = request.getHeader("X-Auth-Role");

        if (userId != null && role != null) {
            CustomUser customUser = new CustomUser(userId, role);

            String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;
            log.info("[HeaderAuthFilter] authority: {}", authority);

            UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                    customUser,
                    null
                    , List.of(new SimpleGrantedAuthority(authority)));

            SecurityContextHolder.getContext().setAuthentication(token);
            log.info("[HeaderAuthFilter] after set Authentication: {}",
                SecurityContextHolder.getContext().getAuthentication());
        }

        filterChain.doFilter(request, response);

    }
}
