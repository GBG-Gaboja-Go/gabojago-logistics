package com.gbg.orderservice.infrastructure.repository;

import com.gbg.orderservice.domain.entity.Order;
import com.gbg.orderservice.domain.entity.QOrder;
import com.gbg.orderservice.domain.repository.OrderRepository;
import com.gbg.orderservice.presentation.dto.request.OrderSearchRequestDto;
import com.gbg.orderservice.presentation.dto.response.GetOrderResponseDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository, OrderRepositoryCustom {

    private final OrderJpaRepository orderJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return orderJpaRepository.findById(orderId);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderJpaRepository.findAll(pageable);
    }

    @Override
    public Page<Order> findByUserId(UUID userId, Pageable pageable) {
        return orderJpaRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<GetOrderResponseDto> searchOrders(OrderSearchRequestDto searchRequestDto,
        Pageable pageable, String role, UUID userId) {
        // 1) predicate 조립 (role/user + 검색DTO 필터)
        BooleanExpression predicate = buildPredicate(searchRequestDto, role, userId);

        // 2) 정렬 스펙 생성
        List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);

        // 3) 기본 쿼리 (엔티티 리스트 조회용)
        com.querydsl.jpa.impl.JPAQuery<Order> query = queryFactory
            .selectFrom(QOrder.order)
            .where(predicate);

        // 4) 전체 카운트 (조건만 동일하게)
        Long total = queryFactory
            .select(QOrder.order.count())
            .from(QOrder.order)
            .where(predicate)
            .fetchOne();
        if (total == null) {
            total = 0L;
        }

        // 5) 페이징 + 정렬 적용하여 실제 데이터 조회
        List<Order> resultList = query
            .orderBy(orders.toArray(new OrderSpecifier[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // 6) DTO 매핑
        List<GetOrderResponseDto> content = resultList.stream()
            .map(Order::toResponseDto)
            .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, total);
    }

    private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (pageable.getSort() != null) {
            for (Sort.Order sortOrder : pageable.getSort()) {
                com.querydsl.core.types.Order direction =
                    sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC
                        : com.querydsl.core.types.Order.DESC;

                switch (sortOrder.getProperty()) {
                    case "createdAt" ->
                        orders.add(new OrderSpecifier<>(direction, QOrder.order.createdAt));
                    case "status" ->
                        orders.add(new OrderSpecifier<>(direction, QOrder.order.status));
                    default -> { /* 정렬 조건이 없거나 매칭 안되면 무시 */ }
                }
            }
        }

        return orders;
    }

    /**
     * predicate 빌더: role/user 체크 + searchRequestDto 기반 필터 결합
     */
    private BooleanExpression buildPredicate(OrderSearchRequestDto searchRequestDto, String role,
        UUID userId) {
        BooleanExpression p = null;

        // role/user 체크
        BooleanExpression userExpr = userCheck(role, userId);
        p = and(p, userExpr);

        // searchRequestDto 내부 필터가 있는 경우 처리 (안정적으로 null 체크)
        if (searchRequestDto != null && searchRequestDto.getOrder() != null) {
            OrderSearchRequestDto.OrderDto od = searchRequestDto.getOrder();

            if (od.getUserId() != null) {
                p = and(p, QOrder.order.userId.eq(od.getUserId()));
            }
            if (od.getProducerVendorId() != null) {
                p = and(p, QOrder.order.producerVendorId.eq(od.getProducerVendorId()));
            }
            if (od.getReceiverVendorId() != null) {
                p = and(p, QOrder.order.receiverVendorId.eq(od.getReceiverVendorId()));
            }
            if (od.getProductId() != null) {
                p = and(p, QOrder.order.productId.eq(od.getProductId()));
            }
            if (od.getStatus() != null) {
                p = and(p, QOrder.order.status.eq(od.getStatus()));
            }
            if (od.getDateFrom() != null) {
                p = and(p, QOrder.order.createdAt.goe(od.getDateFrom().atStartOfDay()));
            }
            if (od.getDateTo() != null) {
                // dateTo 끝날짜의 마지막 시각까지 포함하려면 다음과 같이 처리
                p = and(p, QOrder.order.createdAt.loe(od.getDateTo().atTime(23, 59, 59)));
            }
            // 만약 orderItemIds 같은 리스트 필터가 있으면 아래처럼 추가 (타입/필드명 검토 필요)
            // p = and(p, orderItemIdsIn(od.getOrderItemIds()));
        }

        return p;
    }

    /**
     * null-safe AND 결합 헬퍼
     */
    private BooleanExpression and(BooleanExpression a, BooleanExpression b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return a.and(b);
    }

    /**
     * 기존 userCheck 수정: createdBy는 UUID 타입이므로 변환 후 비교 (파싱 실패 시 null 반환 -> 필터 없음)
     */
    private BooleanExpression userCheck(String role, UUID userId) {
        if (role == null) {
            return null;
        }
        if ("MEMBER".equalsIgnoreCase(role)) {
            if (userId == null) {
                return null;
            }
            try {
                return QOrder.order.createdBy.eq(userId);
            } catch (IllegalArgumentException e) {
                // userId가 UUID 형식이 아니면 필터 적용 안함(또는 필요시 예외 던지기)
                return null;
            }
        }
        // ROLE이 MEMBER가 아니면 필터 없음 (관리자 등 전체 조회 허용)
        return null;
    }

}
