package com.gbg.hubservice.presentation.dto.response;

import com.gbg.hubservice.domain.entity.Hub;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetHubsResponseDto {

    private HubPageDto hubPage;

    public static GetHubsResponseDto of(Page<Hub> hubPage) {
        return GetHubsResponseDto.builder()
            .hubPage(HubPageDto.from(hubPage))
            .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubPageDto {

        private List<HubDto> content;
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;

        public static HubPageDto from(Page<Hub> page) {
            return HubPageDto.builder()
                .content(HubDto.from(page.getContent()))
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class HubDto {

            private UUID id;
            private String name;
            private String address;
            private BigDecimal latitude;
            private BigDecimal longitude;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;

            public static List<HubDto> from(List<Hub> hubs) {
                return hubs.stream().map(HubDto::from).toList();
            }

            public static HubDto from(Hub hub) {
                return HubDto.builder()
                    .id(hub.getId())
                    .name(hub.getName())
                    .address(hub.getAddress())
                    .latitude(hub.getLatitude())
                    .longitude(hub.getLongitude())
                    .createdAt(hub.getCreatedAt())
                    .updatedAt(hub.getUpdatedAt())
                    .build();
            }
        }
    }
}
