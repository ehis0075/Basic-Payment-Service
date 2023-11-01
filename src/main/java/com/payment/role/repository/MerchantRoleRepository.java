package com.payment.role.repository;


import com.payment.merchant.model.Merchant;
import com.payment.role.model.MerchantRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantRoleRepository extends JpaRepository<MerchantRole, Long> {
    MerchantRole findByNameAndMerchant_MerchantId(String name, String merchant_merchantId);

    MerchantRole findByIdAndMerchant_MerchantId(Long id, String merchant_merchantId);

    boolean existsByNameAndMerchant_MerchantId(String name, String merchant_merchantId);
    Optional<MerchantRole> findByName(String name);
}
