package com.gbg.deliveryservice.application.service;

import com.gbg.deliveryservice.presentation.dto.request.InternalCreateAIRequestDto;
import com.gbg.deliveryservice.presentation.dto.response.GetAIResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AIService {

    void createShippingDeadline(InternalCreateAIRequestDto requestDto);

    Page<GetAIResponseDto> getAllLogs(Pageable pageable);

}
