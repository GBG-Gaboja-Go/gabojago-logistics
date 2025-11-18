package com.gbg.orderservice.infrastructure.repository;

import com.gbg.orderservice.domain.entity.Order;
import com.gbg.orderservice.domain.entity.QOrder;
import com.gbg.orderservice.presentation.dto.request.OrderSearchRequestDto;
import com.gbg.orderservice.presentation.dto.response.GetOrderResponseDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
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
public class OrderQueryDslRepositoryImpl implements OrderQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final QOrder order = QOrder.order;

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
        BooleanExpression finalPredicate = null;

        // 1. 역할 기반 접근 제어 조건
        BooleanExpression accessFilter = userAccessFilter(searchRequestDto, role, userId);
        finalPredicate = and(finalPredicate, accessFilter);

        // 2. 검색 DTO 조건
        BooleanExpression searchFilter = searchDtoFilter(searchRequestDto);
        finalPredicate = and(finalPredicate, searchFilter);

        return finalPredicate;
    }

    private BooleanExpression and(BooleanExpression a, BooleanExpression b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return a.and(b);
    }


    private BooleanExpression userAccessFilter(OrderSearchRequestDto searchRequestDto, String role,
        UUID userId) {
        OrderSearchRequestDto.OrderDto od =
            searchRequestDto != null ? searchRequestDto.getOrder() : null;

        if (od == null) {
            return null;
        }

        if ("MASTER".equalsIgnoreCase(role)) {
            // MASTER: 어떤 필터도 걸지 않고 모든 주문 조회 허용
            return null;
        }

        if ("VENDOR_MANAGER".equalsIgnoreCase(role)) {
            // VENDOR_MANAGER: 수령 업체 ID (receiverVendorId)는 본인 업체 ID와 일치해야 함
            if (od.getUserId() != null) {
                return order.userId.eq(od.getUserId());
            }
            // 만약 Service에서 ID 설정을 실패했거나 누락됐다면, 접근 금지 처리 (빈 결과)
            return Expressions.asBoolean(false).isTrue();
        }

        if ("HUB_MANAGER".equalsIgnoreCase(role)) {
            // HUB_MANAGER: 허브 ID (producerHubId)는 본인이 관리하는 허브 ID와 일치해야 함
            // Service에서 이미 producerHubId에 허브 ID가 설정되어 넘어옴
            if (od.getProducerHubId() != null) {
                return order.producerHubId.eq(od.getProducerHubId());
            }
            // 만약 Service에서 ID 설정을 실패했거나 누락됐다면, 접근 금지 처리
            return Expressions.asBoolean(false).isTrue();
        }
        return null;
    }

    private BooleanExpression searchDtoFilter(OrderSearchRequestDto searchRequestDto) {
        if (searchRequestDto == null || searchRequestDto.getOrder() == null) {
            return null;
        }

        OrderSearchRequestDto.OrderDto od = searchRequestDto.getOrder();
        BooleanExpression p = null;

        if (od.getUserId() != null) {
            p = and(p, order.userId.eq(od.getUserId()));
        }
        if (od.getProducerVendorId() != null) {
            p = and(p, order.producerVendorId.eq(od.getProducerVendorId()));
        }
        if (od.getReceiverVendorId() != null) {
            p = and(p, order.receiverVendorId.eq(od.getReceiverVendorId()));
        }
        if (od.getProductId() != null) {
            p = and(p, order.productId.eq(od.getProductId()));
        }
        if (od.getStatus() != null) {
            p = and(p, order.status.eq(od.getStatus()));
        }
        if (od.getDateFrom() != null) {
            p = and(p, order.createdAt.goe(od.getDateFrom().atStartOfDay()));
        }
        if (od.getDateTo() != null) {
            p = and(p, order.createdAt.loe(od.getDateTo().atTime(23, 59, 59)));
        }
        if (od.getProducerHubId() != null) {
            p = and(p, order.producerHubId.eq(od.getProducerHubId()));
        }

        return p;
    }
}
