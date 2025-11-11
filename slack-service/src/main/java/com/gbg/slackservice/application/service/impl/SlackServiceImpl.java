package com.gbg.slackservice.application.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.gabojago.exception.AppException;
import com.gbg.slackservice.application.service.SlackService;
import com.gbg.slackservice.domain.entity.Slack;
import com.gbg.slackservice.domain.repository.SlackRepository;
import com.gbg.slackservice.infrastructure.exception.SlackError;
import com.gbg.slackservice.presentation.dto.response.SlackResponseDto;
import com.gbg.slackservice.presentation.dto.response.SlackVerifyResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SlackServiceImpl implements SlackService {

    @Value("${slack.bot.token}")
    private String slackBotToken;

    private final WebClient webClient = WebClient.create();
    private final SlackRepository slackRepository;

    @Override
    public SlackVerifyResponse verifySlackMember(String email) {
        JsonNode response = webClient.get()
            .uri("https://slack.com/api/users.lookupByEmail?email=" + email)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + slackBotToken)
            .retrieve()
            .bodyToMono(JsonNode.class)
            .block();

        boolean ok = response.path("ok").asBoolean();
        String userId = ok ? response.path("user").path("id").asText() : null;

        return new SlackVerifyResponse(ok, userId);
    }

    @Override
    public void sendVerifySuccessMessage(String channelId, String text) {
        webClient.post()
            .uri("https://slack.com/api/chat.postMessage")
            .header("Authorization", "Bearer " + slackBotToken)
            .bodyValue(Map.of("channel", channelId, "text", text))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    @Override
    public void sendDm(String email, String message) {

        boolean success = false;
        String receiverId = null;

        try {
            // 이메일
            JsonNode lookupResponse = webClient.get()
                .uri("https://slack.com/api/users.lookupByEmail?email=" + email)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + slackBotToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

            if (!lookupResponse.path("ok").asBoolean()) {
                throw new AppException(SlackError.SLACK_USER_NOT_FOUND);
            }

            receiverId = lookupResponse.path("user").path("id").asText();

            // DM 채널
            JsonNode openResponse = webClient.post()
                .uri("https://slack.com/api/conversations.open")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + slackBotToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("users", receiverId))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

            String channelId = openResponse.path("channel").path("id").asText();

            // 메시지 전송
            webClient.post()
                .uri("https://slack.com/api/chat.postMessage")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + slackBotToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("channel", channelId, "text", message))
                .retrieve()
                .bodyToMono(String.class)
                .block();

            success = true;
        } finally {
            // Slack DB 저장
            Slack slack = Slack.of(receiverId != null ? receiverId : "UNKNOWN", message);
            slack.updateSuccess(success);

            slackRepository.save(slack);
        }

    }

    @Override
    public SlackResponseDto slackLogs() {

        List<Slack> slacks = slackRepository.findAll();
        List<SlackResponseDto.SlackDto> slackDtos = slacks.stream()
            .map(s -> SlackResponseDto.SlackDto.builder()
                .id(s.getId())
                .receiverId(s.getReceiverId())
                .content(s.getContent())
                .success(s.isSuccess())
                .createdBy(s.getCreatedBy())
                .build())
            .toList();

        return SlackResponseDto.builder()
            .slacks(slackDtos)
            .build();
    }
}
