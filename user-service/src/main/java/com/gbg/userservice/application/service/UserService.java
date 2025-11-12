package com.gbg.userservice.application.service;

import com.gbg.userservice.domain.entity.User;
import com.gbg.userservice.domain.entity.UserRole;
import com.gbg.userservice.domain.entity.UserStatus;
import com.gbg.userservice.infrastructure.config.auth.CustomUser;
import com.gbg.userservice.presentation.dto.request.AdminUpdateRequestDto;
import com.gbg.userservice.presentation.dto.request.UserUpdateRequestDto;
import com.gbg.userservice.presentation.dto.response.UserListResponseDto;
import com.gbg.userservice.presentation.dto.response.UserResponseDto;
import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserListResponseDto> getUserList();

    UserResponseDto userDetail(UUID userId);

    UUID userDetailUpdate(UserUpdateRequestDto req, UUID id);

    void userDelete(UUID loginId, UUID userId);

    UUID adminDetailUpdate(UUID loginId, UUID userId, AdminUpdateRequestDto req);

    UserStatus getUserStatus(UUID userId);

    User getUser(UUID userId);

    User getUserByEmail(String email);
}
