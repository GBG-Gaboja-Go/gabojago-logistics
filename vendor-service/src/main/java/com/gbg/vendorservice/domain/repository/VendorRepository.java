package com.gbg.vendorservice.domain.repository;

import com.gbg.vendorservice.domain.entity.Vendor;
import com.gbg.vendorservice.presentation.dto.request.SearchVendorRequestDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface VendorRepository {

    Vendor save(Vendor vendor);

    Optional<Vendor> findById(UUID id);

    void delete(Vendor vendor);

    List<Vendor> findAll();

    @Query("SELECT v FROM Vendor v WHERE v.vendorManagerId = :managerId AND v.isDeleted = false")
    Page<Vendor> search(SearchVendorRequestDto dto, Pageable pageable);

    List<Vendor> findAllByVendorManagerId(UUID managerId);
}
