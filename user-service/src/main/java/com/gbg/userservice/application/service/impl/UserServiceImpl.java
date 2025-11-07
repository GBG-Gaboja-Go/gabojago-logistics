package com.gbg.userservice.application.service.impl;

import com.gabojago.exception.AppException;
import com.gbg.userservice.application.service.UserService;
import com.gbg.userservice.domain.entity.User;
import com.gbg.userservice.domain.repository.UserRepository;
import com.gbg.userservice.infrastructure.exception.UserErrorCode;
import com.gbg.userservice.presentation.dto.request.AdminUpdateRequestDto;
import com.gbg.userservice.presentation.dto.request.UserUpdateRequestDto;
import com.gbg.userservice.presentation.dto.response.UserListResponseDto;
import com.gbg.userservice.presentation.dto.response.UserResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserListResponseDto> getUserList() {

        List<User> userList = userRepository.userList();

        return userList.stream()
            .map(u -> UserListResponseDto.builder()
                .user(UserListResponseDto.UserDto.builder()
                    .username(u.getUsername())
                    .nickname(u.getNickname())
                    .slackEmail(u.getSlackEmail())
                    .organization(u.getOrganization())
                    .role(u.getRole())
                    .build())
                .build()).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto userDetail(UUID id) {
        User findUser = getUser(id);

        return UserResponseDto.builder()
            .user(UserResponseDto.UserDto.builder()
                .id(findUser.getId())
                .username(findUser.getUsername())
                .nickname(findUser.getNickname())
                .slackEmail(findUser.getSlackEmail())
                .organization(findUser.getOrganization())
                .summary(findUser.getSummary())
                .role(findUser.getRole())
                .status(findUser.getStatus())
                .build())
            .build();
    }

    @Override
    @Transactional
    public UUID userDetailUpdate(UserUpdateRequestDto req, UUID id) {
        User findUser = getUser(id);

        if (req.nickname() != null) {
            findUser.changeNickname(req.nickname());
        }

        if (req.organization() != null) {
            findUser.changeOrganization(req.organization());
        }

        if (req.summary() != null) {
            findUser.changeSummary(req.summary());
        }

        return findUser.getId();
    }

    @Override
    @Transactional
    public void userDelete(UUID loginId ,UUID userId) {
        User findUser = getUser(userId);

        findUser.delete(loginId);
    }

    @Override
    @Transactional
    public UUID adminDetailUpdate(UUID loginId, UUID userId,  AdminUpdateRequestDto req) {
        User findUser = userRepository.findById(userId).orElseThrow(
            () -> new AppException(UserErrorCode.USER_NOT_FOUND)
        );

        if (req.nickname() != null) {
            findUser.changeNickname(req.nickname());
        }

        if (req.organization() != null) {
            findUser.changeOrganization(req.organization());
        }

        if (req.summary() != null) {
            findUser.changeSummary(req.summary());
        }

        if (req.role() != null) {
            findUser.changeRole(req.role());
        }

        if (req.status() != null) {
            findUser.changeStatus(req.status());
        }

        return findUser.getId();
    }

    private User getUser(UUID userId) {

        User user = userRepository.findById(userId).orElseThrow(
            () -> new AppException(UserErrorCode.USER_NOT_FOUND)
        );

        if (user.getDeletedAt() != null || user.getDeletedBy() != null) {
            throw new AppException(UserErrorCode.USER_NOT_FOUND);
        }

        return user;
    }
}