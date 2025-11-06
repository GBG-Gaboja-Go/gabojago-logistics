package com.gbg.userservice.application.service;

import com.gbg.userservice.presentation.dto.request.UserUpdateRequestDto;
import com.gbg.userservice.presentation.dto.response.UserListResponseDto;
import com.gbg.userservice.presentation.dto.response.UserResponseDto;
import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserListResponseDto> getUserList();

    UserResponseDto userDetail(UUID userId);

    UUID userDetailUpdate(UserUpdateRequestDto req, UUID id);
}