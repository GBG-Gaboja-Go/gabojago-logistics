package com.gbg.vendorservice.infrastructure.repository;

import com.gbg.vendorservice.domain.entity.Vendor;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaVendorRepository extends JpaRepository<Vendor, UUID> {

    Page<Vendor> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Vendor> findAllByManagerId(UUID managerId);

    Page<Vendor> findByIsSupplierTrue(Pageable pageable);

    Page<Vendor> findByIsReceiverTrue(Pageable pageable);

    Optional<Vendor> findByManagerId(UUID managerId);


}
