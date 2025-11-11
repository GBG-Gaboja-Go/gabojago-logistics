package com.gbg.slackservice.application.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.gbg.slackservice.application.service.SlackService;
import com.gbg.slackservice.presentation.dto.response.SlackVerifyResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class SlackServiceImpl implements SlackService {

    @Value("${slack.bot.token}")
    private String slackBotToken;

    private final WebClient webClient = WebClient.create();

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
}
