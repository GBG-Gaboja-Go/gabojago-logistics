package com.gbg.userservice.application.service.impl;

import com.gbg.userservice.application.service.UserService;
import com.gbg.userservice.domain.entity.User;
import com.gbg.userservice.domain.repository.UserRepository;
import com.gbg.userservice.presentation.dto.response.UserResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponseDto> getUserList() {

        List<User> userList = userRepository.userList();

        return userList.stream()
            .map(u -> UserResponseDto.builder()
                .user(UserResponseDto.UserDto.builder()
                    .username(u.getUsername())
                    .nickname(u.getNickname())
                    .slackEmail(u.getSlackEmail())
                    .organization(u.getOrganization())
                    .role(u.getRole())
                    .build())
                .build()).toList();
    }
}