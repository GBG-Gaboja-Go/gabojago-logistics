package com.gbg.deliveryservice.presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gabojago.dto.PageResponseDto;
import com.gbg.deliveryservice.presentation.dto.response.GetAIResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/ai")
public class AIController {

    @GetMapping("/logs")
    public ResponseEntity<BaseResponseDto<PageResponseDto<GetAIResponseDto>>> getAllLogs(
        Pageable pageable
    ) {
        List<GetAIResponseDto> allLogs = IntStream.range(0, 20)
            .mapToObj(i -> GetAIResponseDto.builder()
                .ai(GetAIResponseDto.AIDto.builder()
                    .orderId(UUID.randomUUID())
                    .orderRequestMessage("12월 " + (12 + i % 10) + "일 3시까지는 보내주세요!")
                    .aiResponseMessage(
                        "주문 번호: " + (i + 1) + "\n상품 정보: 샘플 상품 " + (i + 1) + "박스\n[DUMMY]")
                    .finalDispatchBy(LocalDateTime.now().plusDays(i))
                    .build())
                .build())
            .toList();

        // 페이지 처리: 기본 pageSize 10으로 제한
        int pageSize = Math.min(pageable.getPageSize(), 10);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageSize, allLogs.size());
        List<GetAIResponseDto> pageContent = allLogs.subList(start, end);

        Page<GetAIResponseDto> result = new PageImpl<>(pageContent, pageable, allLogs.size());

        return ResponseEntity.ok(
            BaseResponseDto.success("ai 로그 전체 조회", PageResponseDto.from(result), HttpStatus.OK));
    }

}
