package com.gbg.vendorservice.infrastructure.repository;

import com.gbg.vendorservice.domain.entity.Vendor;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaVendorRepository extends JpaRepository<Vendor, UUID> {

    Page<Vendor> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Vendor> findAllByVendorManagerId(UUID managerId);

    Page<Vendor> findBySupplierTrue(Pageable pageable);

    Page<Vendor> findByReceiverTrue(Pageable pageable);

    Page<Vendor> findByNameContainingIgnoreCaseAndSupplierTrue(String name, Pageable pageable);

    Page<Vendor> findByNameContainingIgnoreCaseAndReceiverTrue(String name, Pageable pageable);

    List<Vendor> findByHubId(UUID hubId);

}
