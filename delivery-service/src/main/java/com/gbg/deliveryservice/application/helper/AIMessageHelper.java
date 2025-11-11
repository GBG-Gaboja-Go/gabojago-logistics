package com.gbg.deliveryservice.application.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbg.deliveryservice.presentation.dto.request.InternalCreateAIRequestDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AIMessageHelper {

    private final ObjectMapper objectMapper;

    public String buildPromptForDeadline(
        InternalCreateAIRequestDto req
    ) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("""
                당신은 물류 전문가입니다. 주어진 정보를 바탕으로 최종 발송 마감 시간을 계산하세요.\\n" +
                            "응답은 반드시 ISO-8601 형식의 날짜시간만 출력하세요. 다른 텍스트는 절대 포함하지 마세요.\\n" +
                            "예시: 2025-12-10T09:00:00+09:00\\n\\n 다음 정보를 바탕으로 최종 발송 시한을 계산하세요:

                """)
            .append("주문시간: ").append(req.getAi().getOrderTime()).append("\n")
            .append("주문요청사항: ").append(escape(req.getAi().getOrderRequestMessage())).append("\n")
            .append("픽업허브: ").append(serialize(req.getAi().getPickupHub())).append("\n")
            .append("경유허브: ").append(serialize(req.getAi().getViaHub())).append("\n")
            .append("도착허브: ").append(serialize(req.getAi().getDestinationHub())).append("\n")
            .append("배송주소: ").append(req.getAi().getDestinationAddress()).append("\n")
            .append("드라이버 근무시간: ")
            .append(req.getAi().getDriverWorkTime() != null
                ? String.format("%02d:00-%02d:00",
                req.getAi().getDriverWorkTime().getStartHour(),
                req.getAi().getDriverWorkTime().getEndHour())
                : "09:00-18:00")
            .append("\n\n")
            .append("위 정보를 고려하여 최종 발송 시한을 ISO-8601 형식으로만 출력하세요.");

        return prompt.toString();
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\n", "\\n");
    }

    private String serialize(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            return "\"\"";
        }
    }

    public LocalDateTime parseFinalDeadlineOnly(String aiRaw, LocalDateTime referenceOrderTime) {
        if (aiRaw == null) {
            return null;
        }

        // 1) ISO-8601 찾기 (간단한 정규식)
        Pattern iso = Pattern.compile(
            "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:Z|[+-]\\d{2}:?\\d{2})?");
        Matcher mIso = iso.matcher(aiRaw);
        if (mIso.find()) {
            String isoStr = mIso.group();
            try {
                // Offset 포함하면 OffsetDateTime로, 아니면 LocalDateTime로
                if (isoStr.endsWith("Z") || isoStr.contains("+")
                    || isoStr.contains("-") && isoStr.length() > 19) {
                    return java.time.OffsetDateTime.parse(isoStr)
                        .atZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();
                } else {
                    return LocalDateTime.parse(isoStr);
                }
            } catch (Exception e) {
                // 파싱 실패하면 계속해서 한국어 시도
            }
        }

        // 2) 한국어 날짜 표현 (예: 12월 10일 오전 9시, 12/10 09:00 등)
        Pattern kor = Pattern.compile(
            "(\\d{1,2})월\\s*(\\d{1,2})일\\s*(오전|오후)?\\s*(\\d{1,2})시(?:\\s*(\\d{1,2})분)?");
        Matcher mKor = kor.matcher(aiRaw);
        if (mKor.find()) {
            try {
                int month = Integer.parseInt(mKor.group(1));
                int day = Integer.parseInt(mKor.group(2));
                String ampm = mKor.group(3);
                int hour = Integer.parseInt(mKor.group(4));
                String minG = mKor.group(5);
                int minute = (minG != null && !minG.isBlank()) ? Integer.parseInt(minG) : 0;

                if ("오후".equals(ampm) && hour < 12) {
                    hour += 12;
                }
                if ("오전".equals(ampm) && hour == 12) {
                    hour = 0;
                }

                int year = (referenceOrderTime != null) ? referenceOrderTime.getYear()
                    : LocalDate.now(ZoneId.of("Asia/Seoul")).getYear();

                // 연도 보정: 만약 파싱된 날짜가 reference보다 과거(예: 주문이 2025-01-01인데 12월이면 전년도일 수 있음)
                LocalDateTime candidate = LocalDateTime.of(year, month, day, hour, minute);
                if (referenceOrderTime != null && candidate.isBefore(
                    referenceOrderTime.minusYears(1))) {
                    candidate = candidate.plusYears(1);
                }
                return candidate;
            } catch (Exception e) { /* fallback */ }
        }

        // 3) 숫자+시간 조합(예: "12-10 09:00" 등) 시도(간단)
        Pattern simple = Pattern.compile("(\\d{1,2})[\\-/](\\d{1,2})\\s*(\\d{1,2}):(\\d{2})");
        Matcher mS = simple.matcher(aiRaw);
        if (mS.find()) {
            try {
                int month = Integer.parseInt(mS.group(1));
                int day = Integer.parseInt(mS.group(2));
                int hour = Integer.parseInt(mS.group(3));
                int minute = Integer.parseInt(mS.group(4));
                int year = (referenceOrderTime != null) ? referenceOrderTime.getYear()
                    : LocalDate.now(ZoneId.of("Asia/Seoul")).getYear();
                return LocalDateTime.of(year, month, day, hour, minute);
            } catch (Exception e) { /* fallback */ }
        }

        return null; // 못 찾음
    }
}
