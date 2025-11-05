package com.gbg.userservice.application.service;

import com.gbg.userservice.presentation.dto.response.UserResponseDto;
import java.util.List;

public interface UserService {

    List<UserResponseDto> getUserList();
}