package com.payment.merchant.repository;


import com.payment.merchant.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    boolean existsByName(String name);

    Merchant findByMerchantId(String merchantId);

    boolean existsByMerchantId(String merchantId);

}
