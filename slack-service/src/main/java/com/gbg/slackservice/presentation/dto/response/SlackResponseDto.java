package com.gbg.slackservice.presentation.dto.response;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackResponseDto {

    List<SlackDto> slacks;

    @Getter
    @Builder
    public static class SlackDto {
        UUID id;
        String receiverId;
        String content;
        boolean success;
        UUID createdBy;
    }
}
