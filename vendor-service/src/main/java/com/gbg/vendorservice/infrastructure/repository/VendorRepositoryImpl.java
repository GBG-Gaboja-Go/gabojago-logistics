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
    public List<Vendor> findAllByVendorManagerId(UUID managerId) {
        return jpaVendorRepository.findAllByVendorManagerId(managerId);
    }


    @Override
    public Page<Vendor> search(SearchVendorRequestDto dto, Pageable pageable) {
        String name = dto.getName();
        String type = dto.getType();

        // 🔧 안정성을 위한 전처리
        if (type != null) {
            type = type.trim().toLowerCase(); // " supplier" → "supplier"
        }

        boolean hasName = name != null && !name.trim().isEmpty();
        boolean searchSupplier = "supplier".equals(type);
        boolean searchReceiver = "receiver".equals(type);

        // ✅ 디버깅 로그 (확인용)
        System.out.println("[Vendor Search] name: " + name + ", type: " + type);
        System.out.println(
            "→ hasName: " + hasName + ", isSupplier: " + searchSupplier + ", isReceiver: "
                + searchReceiver);

        // ✅ 조건 조합별 분기 처리
        if (hasName && searchSupplier) {
            return jpaVendorRepository.findByNameContainingIgnoreCaseAndSupplierTrue(name,
                pageable);
        } else if (hasName && searchReceiver) {
            return jpaVendorRepository.findByNameContainingIgnoreCaseAndReceiverTrue(name,
                pageable);
        } else if (hasName) {
            return jpaVendorRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (searchSupplier) {
            return jpaVendorRepository.findBySupplierTrue(pageable);
        } else if (searchReceiver) {
            return jpaVendorRepository.findByReceiverTrue(pageable);
        } else {
            return jpaVendorRepository.findAll(pageable);
        }
    }

    @Override
    public List<Vendor> findByHubId(UUID hubId) {
        return jpaVendorRepository.findByHubId(hubId);
    }

    @Override
    public boolean existsById(UUID vendorId) {
        return jpaVendorRepository.existsById(vendorId);
    }
}
