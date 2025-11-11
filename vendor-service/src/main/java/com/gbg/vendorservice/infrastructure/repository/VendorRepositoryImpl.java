package com.gbg.vendorservice.infrastructure.repository;

import com.gbg.vendorservice.domain.entity.Vendor;
import com.gbg.vendorservice.domain.repository.VendorRepository;
import com.gbg.vendorservice.presentation.dto.request.SearchVendorRequestDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VendorRepositoryImpl implements VendorRepository {

    private final JpaVendorRepository jpaVendorRepository;

    @Override
    public Vendor save(Vendor vendor) {
        return jpaVendorRepository.save(vendor);
    }

    @Override
    public Optional<Vendor> findById(UUID id) {
        return jpaVendorRepository.findById(id);
    }

    @Override
    public void delete(Vendor vendor) {
        jpaVendorRepository.delete(vendor);
    }

    @Override
    public List<Vendor> findAll() {
        return jpaVendorRepository.findAll();
    }

    @Override
    public List<Vendor> findAllByManagerId(UUID managerId) {
        return jpaVendorRepository.findAllByManagerId(managerId);
    }


    @Override
    public Page<Vendor> search(SearchVendorRequestDto dto, Pageable pageable) {
        String name = dto.getName();
        String type = dto.getType();

        if (name != null && !name.isEmpty()) {
            return jpaVendorRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if ("supplier".equalsIgnoreCase(type)) {
            return jpaVendorRepository.findByIsSupplierTrue(pageable);
        } else if ("receiver".equalsIgnoreCase(type)) {
            return jpaVendorRepository.findByIsReceiverTrue(pageable);
        } else {
            return jpaVendorRepository.findAll(pageable);
        }
    }
}
