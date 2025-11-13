package com.gbg.userservice.application.service.impl;

import com.gabojago.exception.AppException;
import com.gbg.userservice.application.service.AuthService;
import com.gbg.userservice.domain.entity.User;
import com.gbg.userservice.domain.entity.UserRole;
import com.gbg.userservice.domain.repository.UserRepository;
import com.gbg.userservice.infrastructure.config.jwt.JwtTokenProvider;
import com.gbg.userservice.infrastructure.exception.UserErrorCode;
import com.gbg.userservice.presentation.dto.request.CreateUserRequestDto;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UUID signUp(CreateUserRequestDto req) {

        validateDuplicateUsernameAndSlackEmail(req.username(), req.slackEmail());

        if (req.hasCompanyId()) {
            User user = User.of(
                req.username(),
                req.nickname(),
                req.slackEmail(),
                passwordEncoder.encode(req.password()),
                req.organization(),
                req.summary());
            User saveUser = userRepository.save(user);
            return saveUser.getId();
        }
        else {
            User user = User.of(
                req.username(),
                req.nickname(),
                req.slackEmail(),
                passwordEncoder.encode(req.password()),
                req.summary());
            User saveUser = userRepository.save(user);
            return saveUser.getId();
        }
    }

    @Override
    public void logout(String accessToken, String refreshToken) {

        // Bearer 제거
        String token = accessToken.replace("Bearer ","").trim();

        // Access Token 검증 및 ID 추출
        UUID userId = jwtTokenProvider.getUserIdFromToken(token);

        // Refresh Token 삭제 (재발급 방지)
        redisTemplate.delete("Refresh Token:" + userId);

        // Access Token 블랙리스트 등록
        long expiration = jwtTokenProvider.getExpiration(token);
        redisTemplate.opsForValue().set(
            "BL:" + token,
            "logout",
            expiration,
            TimeUnit.MILLISECONDS
        );

    }

    private void validateDuplicateUsernameAndSlackEmail(String username, String slackEmail) {

        List <User> users = userRepository.findAllByUsernameOrSlackEmail(username, slackEmail);

        boolean usernameDuplicate = users.stream()
            .anyMatch(u -> u.getUsername().equals(username));

        if (usernameDuplicate) {
            throw new AppException(UserErrorCode.USER_DUPLICATE_NAME);
        }

        boolean slackEmailDuplicate = users.stream()
            .anyMatch(u -> u.getSlackEmail().equals(slackEmail));

        if (slackEmailDuplicate) {
            throw new AppException(UserErrorCode.USER_DUPLICATE_EMAIL);
        }
    }
}
