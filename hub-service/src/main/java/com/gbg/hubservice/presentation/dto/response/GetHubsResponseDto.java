package com.gbg.hubservice.presentation.dto.response;

import com.gbg.hubservice.domain.entity.Hub;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;


@Getter
@Builder
public class GetHubsResponseDto {

    private final HubPageDto hubPage;


    public static GetHubsResponseDto of(Page<Hub> hubPage) {
        return GetHubsResponseDto.builder()
            .hubPage(new HubPageDto(hubPage))
            .build();
    }

    @Getter
    @ToString
    public static class HubPageDto extends PagedModel<HubPageDto.HubDto> {


        public HubPageDto(Page<Hub> hubPage) {
            super(
                new PageImpl<>(
                    HubDto.from(hubPage.getContent()),
                    hubPage.getPageable(),
                    hubPage.getTotalElements()
                )
            );
        }

        public HubPageDto(HubDto... hubArray) {
            super(new PageImpl<>(List.of(hubArray)));
        }

        @Getter
        @Builder
        public static class HubDto {

            private final UUID id;
            private final String name;
            private final String address;
            private final BigDecimal latitude;
            private final BigDecimal longitude;
            private final LocalDateTime createdAt;
            private final LocalDateTime updatedAt;


            public static List<HubDto> from(List<Hub> hubList) {
                return hubList.stream().map(HubDto::from).toList();
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

            public static HubDto of(UUID id, String name, String address,
                BigDecimal latitude, BigDecimal longitude) {
                LocalDateTime now = LocalDateTime.now();
                return HubDto.builder()
                    .id(id)
                    .name(name)
                    .address(address)
                    .latitude(latitude)
                    .longitude(longitude)
                    .createdAt(now)
                    .updatedAt(now)
                    .build();
            }
        }
    }
}
