package com.gbg.slackservice.infrastructure.config;


import com.gbg.slackservice.infrastructure.config.auth.CustomUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomAuditAware implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {

        Authentication authentication
             = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("[AuditorAware] : authentication : " + authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("[AuditorAware] 인증 정보가 없거나 인증되지 않은 상태입니다.");
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        System.out.println("[AuditorAware] : principal : " + principal);

        if (principal instanceof CustomUser user) {
            System.out.println("[AuditorAware] 로그인 사용자 ID : " + user.getUserId());
            return Optional.of(UUID.fromString(user.getUserId()));
        }

        System.out.println("[AuditorAware] Principal이 CustomUser 타입이 아닙니다. 현재 타입 : " + (principal != null ? principal.getClass().getName() : "null"));
        return Optional.empty();
    }

}
