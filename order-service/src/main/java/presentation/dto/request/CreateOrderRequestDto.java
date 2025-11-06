package presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
public class CreateOrderRequestDto {

    @Valid
    @NotNull(message = "주문 정보를 입력해주세요.")
    private OrderDto order;

    @Getter
    @Builder
    public static class OrderDto {

        @NotNull(message = "공급 업체 id을 입력해주세요.")
        private UUID producerVendorId;
        @NotNull(message = "수령 업체 id을 입력해주세요.")
        private UUID receiverVendorId;

        @NotNull(message = "주문 상품을 입력해주세요.")
        private UUID productId;

        @NotNull(message = "주문 상품 수량을 입력해주세요.")
        @Min(value = 1, message = "주문 상품을 1개 이상 입력해주세요.")
        private Integer quantity;

        private String requestMessage;
    }
}
