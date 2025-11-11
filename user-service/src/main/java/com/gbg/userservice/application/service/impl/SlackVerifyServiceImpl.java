package com.gbg.userservice.application.service.impl;

import static com.gbg.userservice.infrastructure.exception.UserErrorCode.NOT_IN_WORKSPACE_EMAIL;
import static com.gbg.userservice.infrastructure.exception.UserErrorCode.USER_NOT_FOUND;

import com.gabojago.dto.BaseResponseDto;
import com.gabojago.exception.AppException;
import com.gbg.userservice.application.service.SlackVerifyService;
import com.gbg.userservice.domain.entity.User;
import com.gbg.userservice.domain.repository.UserRepository;
import com.gbg.userservice.infrastructure.client.SlackFeignClient;
import com.gbg.userservice.presentation.dto.request.SlackVerifyRequest;
import com.gbg.userservice.presentation.dto.request.SlackVerifySuccessRequest;
import com.gbg.userservice.presentation.dto.response.SlackVerifyResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlackVerifyServiceImpl implements SlackVerifyService {

    private final UserRepository userRepository;
    private final SlackFeignClient slackFeignClient;
    private final HttpServletRequest request;

    @Override
    public void verifySlackEmail(UUID id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new AppException(USER_NOT_FOUND)
        );

        String token = request.getHeader("Authorization");
        SlackVerifyRequest slackRequest = new SlackVerifyRequest(user.getSlackEmail());
        BaseResponseDto<SlackVerifyResponse> response = slackFeignClient.verifySlackMember(slackRequest, token);

        if (response.getData().ok()) {
            user.updateSlackVerified(true);

            SlackVerifySuccessRequest msgReq = new SlackVerifySuccessRequest(
                "C09RYJH8XGU",
                "✅" + user.getSlackEmail() + " 님, 슬랙 이메일 인증이 완료되었습니다!"
            );
            slackFeignClient.sendVerifySuccessMessage(msgReq);

        } else {
            throw new AppException(NOT_IN_WORKSPACE_EMAIL);
        }
    }
}
