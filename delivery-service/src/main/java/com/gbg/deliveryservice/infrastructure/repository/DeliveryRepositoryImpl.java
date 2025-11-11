package com.gbg.deliveryservice.infrastructure.repository;

import com.gbg.deliveryservice.domain.entity.Delivery;
import com.gbg.deliveryservice.domain.entity.QDelivery;
import com.gbg.deliveryservice.domain.entity.enums.DeliveryStatus;
import com.gbg.deliveryservice.domain.repository.DeliveryRepository;
import com.gbg.deliveryservice.infrastructure.config.querydsl.QuerydslUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {

    private final DeliveryJpaRepository deliveryJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Delivery save(Delivery delivery) {
        return deliveryJpaRepository.save(delivery);
    }

    @Override
    public Optional<Delivery> findByIdAndDeletedAtIsNull(UUID id) {
        return deliveryJpaRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public boolean existsByOrderIdAndDeletedAtIsNull(UUID orderId) {
        return deliveryJpaRepository.existsByOrderIdAndDeletedAtIsNull(orderId);
    }

    @Override
    public Page<Delivery> deliveryPage(Pageable pageable, DeliveryStatus status) {
        QDelivery delivery = QDelivery.delivery;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(delivery.deletedAt.isNull());
        if (status != null) {
            builder.and(delivery.status.eq(status));
        }

        List<Delivery> deliveries = queryFactory.selectFrom(delivery)
            .where(builder)
            .orderBy(QuerydslUtils.getSort(pageable, delivery))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(delivery.count())
            .from(delivery)
            .where(builder)
            .fetchOne();

        return new PageImpl<>(deliveries, pageable, total);
    }

    @Override
    public Page<Delivery> deliveryMyPage(Pageable pageable, DeliveryStatus status,
        List<UUID> deliveryIdList) {
        QDelivery delivery = QDelivery.delivery;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(delivery.deletedAt.isNull());
        builder.and(delivery.id.in(deliveryIdList));
        if (status != null) {
            builder.and(delivery.status.eq(status));
        }

        List<Delivery> deliveries = queryFactory.selectFrom(delivery)
            .where(builder)
            .orderBy(QuerydslUtils.getSort(pageable, delivery))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(delivery.count())
            .from(delivery)
            .where(builder)
            .fetchOne();

        return new PageImpl<>(deliveries, pageable, total);
    }

}
