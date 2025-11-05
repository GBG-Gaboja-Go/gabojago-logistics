package com.gbg.userservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.userservice.application.service.AuthService;
import com.gbg.userservice.application.service.UserService;
import com.gbg.userservice.infrastructure.config.auth.CustomUser;
import com.gbg.userservice.presentation.dto.request.CreateUserRequestDto;
import com.gbg.userservice.presentation.dto.response.UserResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponseDto<UUID>> signUp(
        @Valid @RequestBody CreateUserRequestDto req
    ) {
        UUID saveUser = authService.signUp(req);

        return ResponseEntity.ok(BaseResponseDto.success("회원가입이 완료 되었습니다", saveUser));
    }

    @GetMapping
    public ResponseEntity<BaseResponseDto<List<UserResponseDto>>> userList() {

        List<UserResponseDto> userList = userService.getUserList();

        return ResponseEntity.ok(BaseResponseDto.success("조회 성공", userList));
    }

}