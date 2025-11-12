package com.gbg.hubservice.infrastructure.repository;

import com.gbg.hubservice.domain.entity.Hub;
import com.gbg.hubservice.domain.repository.HubRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubRepositoryImpl implements HubRepository {

    private final HubJpaRepository jpa;
    private final EntityManager em;

    @Override
    public boolean existsByName(String name) {
        return jpa.existsByName(name);
    }

    @Override
    public Optional<Hub> findById(UUID id) {
        TypedQuery<Hub> query = em.createQuery("""
                select h
                from Hub h
                where h.id = :id and h.deletedAt is null
            """, Hub.class);
        query.setParameter("id", id);
        List<Hub> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Page<Hub> findAll(Pageable pageable) {
        TypedQuery<Hub> contentQuery = em.createQuery("""
                select h
                from Hub h
                where h.deletedAt is null
                order by h.createdAt desc
            """, Hub.class);
        contentQuery.setFirstResult((int) pageable.getOffset());
        contentQuery.setMaxResults(pageable.getPageSize());
        List<Hub> content = contentQuery.getResultList();

        TypedQuery<Long> countQuery = em.createQuery("""
                select count(h)
                from Hub h
                where h.deletedAt is null
            """, Long.class);
        long total = countQuery.getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    @Transactional
    public Hub save(Hub hub) {
        return jpa.save(hub);
    }

    @Override
    public Optional<Hub> findByUserId(UUID userId) {
        return jpa.findByUserId(userId)
            .filter(h -> h.getDeletedAt() == null);
    }

    @Override
    public boolean existsByIdAndIsDeletedFalse(UUID id) {
        return jpa.existsByIdAndDeletedAtIsNull(id);
    }
}
