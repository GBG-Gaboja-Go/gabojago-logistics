package com.gbg.hubservice.infrastructure.config.filter;

import com.gbg.hubservice.infrastructure.config.auth.CustomUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class HeaderAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain) throws ServletException, IOException {

        String idHeader = request.getHeader("X-Auth-Id");
        String roleHeader = request.getHeader("X-Auth-Role");

        if (StringUtils.hasText(idHeader) && StringUtils.hasText(roleHeader)) {
            try {
                UUID userId = UUID.fromString(idHeader.trim());

                String role = roleHeader.trim();
                if (!role.startsWith("ROLE_")) {
                    role = "ROLE_" + role;
                }

                var authorities = List.of(new SimpleGrantedAuthority(role));
                var principal = new CustomUser(userId, authorities);
                var auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (IllegalArgumentException ignore) {
            }
        }

        chain.doFilter(request, response);
    }
}
