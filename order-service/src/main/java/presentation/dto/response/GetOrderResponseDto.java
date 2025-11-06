package presentation.dto.response;

import com.gbg.orderservice.domain.entity.enums.OrderStatus;
import java.math.BigInteger;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetOrderResponseDto {

    private final OrderDto order;

    @Getter
    @Builder
    public static class OrderDto {

        private final UUID id;
        private UUID producerVendorId;
        private UUID receiverVendorId;
        private UUID deliveryId;
        private UUID productId;
        private Integer quantity;
        private BigInteger totalPrice;
        private String requestMessage;
        private OrderStatus status;

    }
}
