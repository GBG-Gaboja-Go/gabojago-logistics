package presentation.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderResponseDto {

    private final OrderDto order;

    @Getter
    @Builder
    public static class OrderDto {

        private final UUID id;
    }

}
