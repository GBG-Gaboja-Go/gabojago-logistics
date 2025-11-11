package com.gbg.vendorservice.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVendorRequestDto {

    @NotNull(message = "업체명을 입력해주세요.")
    private String name;

    @NotNull(message = "허브 ID를 입력해주세요.")
    private UUID hubId;

    @NotNull(message = "매니저 ID를 입력해주세요.")
    private UUID managerId;

    @NotNull(message = "주소를 입력해주세요.")
    private String address;

    @NotNull(message = "공급업체 여부를 입력해주세요.")
    private Boolean isSupplier;

    @NotNull(message = "수령업체 여부를 입력해주세요.")
    private Boolean isReceiver;
}
