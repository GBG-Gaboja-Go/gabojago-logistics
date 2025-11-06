package com.gbg.deliveryservice.presentation.dto.response;

import lombok.Builder;

@Builder
public record PageInfoDTO(
    int pageNumber,
    int pageSize,
    int totalPages,
    long totalElements
) {

    @Builder
    public static PageInfoDTO from(
        int pageNumber,
        int pageSize,
        int totalPages,
        long totalElements
    ) {
        return PageInfoDTO.builder()
            .pageNumber(pageNumber)
            .pageSize(pageSize)
            .totalPages(totalPages)
            .totalElements(totalElements)
            .build();
    }
}