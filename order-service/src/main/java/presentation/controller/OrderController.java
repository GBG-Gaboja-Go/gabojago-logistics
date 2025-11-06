package presentation.controller;

import com.gabojago.dto.BaseResponseDto;
import com.gabojago.dto.PageResponseDto;
import com.gbg.orderservice.domain.entity.enums.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import presentation.dto.request.CreateOrderRequestDto;
import presentation.dto.request.OrderSearchRequestDto;
import presentation.dto.request.UpdateOrderStatusRequestDto;
import presentation.dto.response.CreateOrderResponseDto;
import presentation.dto.response.GetOrderResponseDto;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    @PostMapping
    @Operation(summary = "주문 생성 API", description = "수령업체만 주문을 생성할 수 있습니다.")
    public ResponseEntity<BaseResponseDto<CreateOrderResponseDto>> createOrder(
        @Valid @RequestBody CreateOrderRequestDto requestDto
    ) {
        // order 생성
        CreateOrderResponseDto responseDto = CreateOrderResponseDto.builder()
            .order(
                CreateOrderResponseDto.OrderDto.builder()
                    .id(UUID.randomUUID())
                    .build()
            )
            .build();

        return ResponseEntity.ok(
            BaseResponseDto.success("주문 생성 성공", responseDto, HttpStatus.CREATED));
    }

    @PostMapping("/search")
    @Operation(summary = "주문 전체 조회 API", description = "수령업체는 본인 주문을 모두 조회할 수 있습니다.(마스터는 모든 수령업체 주문 조회 가능)")
    public ResponseEntity<BaseResponseDto<PageResponseDto<GetOrderResponseDto>>> getOrders(
        OrderSearchRequestDto searchRequestDto,
        Pageable pageable
    ) {
        List<GetOrderResponseDto> orders = IntStream.range(0, 5)
            .mapToObj(i -> GetOrderResponseDto.builder()
                .order(GetOrderResponseDto.OrderDto.builder()
                    .id(UUID.randomUUID())
                    .producerVendorId(UUID.randomUUID())
                    .receiverVendorId(UUID.randomUUID())
                    .productId(UUID.randomUUID())
                    .deliveryId(UUID.randomUUID())
                    .quantity(1 + i)
                    .totalPrice(BigInteger.valueOf(10000L * (i + 1)))
                    .requestMessage("샘플 주문 " + i)
                    .status(OrderStatus.PENDING)
                    .build())
                .build())
            .collect(Collectors.toList());

        Page<GetOrderResponseDto> result = new PageImpl<>(
            orders,
            pageable,
            orders.size()
        );

        return ResponseEntity.ok(
            BaseResponseDto.success("메뉴 목록 조회 성공", PageResponseDto.from(result), HttpStatus.OK));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "상세 주문 조회 API", description = "상세 주문을 조회합니다.")
    public ResponseEntity<BaseResponseDto<GetOrderResponseDto>> getOrder(
        @Parameter(description = "order UUID") @PathVariable UUID orderId
    ) {
        GetOrderResponseDto responseDto = GetOrderResponseDto.builder()
            .order(
                GetOrderResponseDto.OrderDto.builder()
                    .id(orderId)
                    .producerVendorId(UUID.randomUUID())
                    .receiverVendorId(UUID.randomUUID())
                    .deliveryId(UUID.randomUUID())
                    .productId(UUID.randomUUID())
                    .quantity(40)
                    .totalPrice(BigInteger.valueOf(400000))
                    .requestMessage("내일 5시까지 배달 부탁드려요.")
                    .status(OrderStatus.DELIVERING)
                    .build()
            )
            .build();
        return ResponseEntity.ok(
            BaseResponseDto.success("상세 주문 조회 성공", responseDto, HttpStatus.OK));
    }

    @PatchMapping("/{orderId}")
    @Operation(summary = "주문 상태 업데이트 API", description = "마스터와 담당 허브 관리자는 상세 주문을 업데이트할 수 있습니다.")
    public ResponseEntity<BaseResponseDto<Void>> updateOrderStatus(
        @Valid @RequestBody UpdateOrderStatusRequestDto requestDto,
        @Parameter(description = "order UUID") @PathVariable UUID orderId
    ) {
        // order status 변경
        return ResponseEntity.ok(BaseResponseDto.success("주문 상태 변경 성공", HttpStatus.NO_CONTENT));
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "주문 취소 API", description = "상품 담당 허브 관리자와 공급업체는 주문을 취소할 수 있습니다.")
    public ResponseEntity<BaseResponseDto<Void>> cancelOrder(
        @Parameter(description = "order UUID") @PathVariable UUID orderId
    ) {
        // order 취소
        return ResponseEntity.ok(BaseResponseDto.success("주문 취소 성공", HttpStatus.NO_CONTENT));
    }
}
