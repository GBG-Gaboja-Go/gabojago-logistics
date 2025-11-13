package com.gbg.userservice.application.service;

import com.gbg.userservice.presentation.dto.request.CreateUserRequestDto;
import java.util.UUID;

public interface AuthService {

    UUID signUp(CreateUserRequestDto req);

    void logout(String accessToken, String refreshToken);
}
