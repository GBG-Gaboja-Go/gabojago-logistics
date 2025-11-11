package com.gbg.userservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gbg.userservice.application.service.AuthService;
import com.gbg.userservice.application.service.UserService;
import com.gbg.userservice.domain.entity.User;
import com.gbg.userservice.domain.entity.UserStatus;
import com.gbg.userservice.infrastructure.config.auth.CustomUser;
import com.gbg.userservice.presentation.dto.request.AdminUpdateRequestDto;
import com.gbg.userservice.presentation.dto.request.CreateUserRequestDto;
import com.gbg.userservice.presentation.dto.request.UserUpdateRequestDto;
import com.gbg.userservice.presentation.dto.response.UserListResponseDto;
import com.gbg.userservice.presentation.dto.response.UserResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BaseResponseDto.success(
                "회원가입이 완료 되었습니다",
                saveUser,
                HttpStatus.CREATED));
    }

    @GetMapping
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<BaseResponseDto<List<UserListResponseDto>>> userList() {

        List<UserListResponseDto> userList = userService.getUserList();

        return ResponseEntity
            .status(HttpStatus.OK)
            .body((BaseResponseDto.success(
                "조회 성공",
                userList,
                HttpStatus.OK)));
    }

    @GetMapping("/my-page")
    public ResponseEntity<BaseResponseDto<UserResponseDto>> userDetail(
        @AuthenticationPrincipal CustomUser customUser
    ) {
        UserResponseDto user = userService.userDetail(customUser.getId());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponseDto.success(
                "조회 성공",
                user,
                HttpStatus.OK));
    }

    @GetMapping("/internal/userId/{userId}")
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<BaseResponseDto<User>> getUser(
        @PathVariable("userId") UUID userId
    ) {

        User user = userService.getUser(userId);
        return ResponseEntity.ok(BaseResponseDto.success(
            "사용자 정보 조회 완료",
            user,
            HttpStatus.OK
        ));
    }

    @GetMapping("/internal/email/{email}")
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<BaseResponseDto<User>> getUserByEmail(
        @PathVariable("email") String email
    ) {

        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(BaseResponseDto.success(
            "유저 객체 반환 완료",
            user,
            HttpStatus.OK
        ));
    }

    @PatchMapping("/my-page")
    public ResponseEntity<BaseResponseDto<UUID>> userDetailUpdate(
        @RequestBody UserUpdateRequestDto req,
        @AuthenticationPrincipal CustomUser customUser
    ) {
        UUID user = userService.userDetailUpdate(req, customUser.getId());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponseDto.success(
                "사용자 정보가 수정되었습니다.",
                user,
                HttpStatus.OK
            ));
    }

    @PatchMapping("/my-page/{userId}")
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<BaseResponseDto<UUID>> adminDetailUpdate(
        @AuthenticationPrincipal CustomUser customUser,
        @RequestBody AdminUpdateRequestDto req,
        @PathVariable("userId") UUID userId
    ) {
        UUID user = userService.adminDetailUpdate(customUser.getId(), userId, req);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponseDto.success(
                "사용자 정보가 정상적으로 수정되었습니다.",
                user,
                HttpStatus.OK
            ));
    }

    @DeleteMapping("/my-page/{userId}")
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<BaseResponseDto<Void>> userDelete(
        @AuthenticationPrincipal CustomUser customUser,
        @PathVariable("userId") UUID userId
    ) {
        userService.userDelete(customUser.getId(), userId);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(BaseResponseDto.success(
                "사용자 삭제가 정상적으로 처리되었습니다.",
                HttpStatus.NO_CONTENT
            ));
    }

    @GetMapping("/internal/{userId}/status")
    public ResponseEntity<BaseResponseDto<UserStatus>> getUserStatus(
        @PathVariable("userId") UUID userId
    ) {

        UserStatus status = userService.getUserStatus(userId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponseDto.success(
                "사용자 상태 정보 조회 성공",
                status,
                HttpStatus.OK
            ));
    }

}
