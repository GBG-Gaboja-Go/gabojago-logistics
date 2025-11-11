package com.gbg.vendorservice.domain.entity;

import com.gabojago.entity.BaseEntity;
import com.gbg.vendorservice.presentation.dto.request.UpdateVendorRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_vendor")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "vendor_manager_id", nullable = false)
    private UUID vendorManagerId;

    @Column(name = "is_supplier", nullable = false)
    private Boolean isSupplier;

    @Column(name = "is_receiver", nullable = false)
    private Boolean isReceiver;

    @Column(nullable = false)
    private String address;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public void update(UpdateVendorRequestDto dto) {
        if (dto.getName() != null) {
            this.name = dto.getName();
        }
        if (dto.getHubId() != null) {
            this.hubId = dto.getHubId();
        }
        if (dto.getVendorMangerId() != null) {
            this.vendorManagerId = dto.getVendorMangerId();
        }
        if (dto.getIsSupplier() != null) {
            this.isSupplier = dto.getIsSupplier();
        }
        if (dto.getIsReceiver() != null) {
            this.isReceiver = dto.getIsReceiver();
        }
        if (dto.getAddress() != null) {
            this.address = dto.getAddress();
        }
    }
}
