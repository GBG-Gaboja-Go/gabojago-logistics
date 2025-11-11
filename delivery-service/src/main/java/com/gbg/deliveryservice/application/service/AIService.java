package com.gbg.deliveryservice.application.service;

import com.gbg.deliveryservice.presentation.dto.request.InternalCreateAIRequestDto;

public interface AIService {

    void createShippingDeadline(InternalCreateAIRequestDto requestDto);
}
