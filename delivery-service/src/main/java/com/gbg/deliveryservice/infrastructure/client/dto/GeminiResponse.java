package com.gbg.deliveryservice.infrastructure.client.dto;

import java.util.List;

public record GeminiResponse(
    List<Candidate> candidates
) {

    // 응답에서 텍스트만 추출하는 헬퍼 메서드
    public String extractText() {
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }
        return candidates.get(0).content().extractText();
    }

    public record Candidate(
        Content content
    ) {

    }

    public record Content(
        List<Part> parts
    ) {

        public String extractText() {
            if (parts == null || parts.isEmpty()) {
                return null;
            }
            // 텍스트만 추출하는 상황이므로 첫 번째 Part의 텍스트를 반환합니다.
            return parts.get(0).text();
        }
    }

    public record Part(
        String text
    ) {

    }
}
