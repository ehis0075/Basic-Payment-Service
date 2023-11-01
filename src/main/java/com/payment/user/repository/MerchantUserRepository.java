package com.payment.user.repository;


import com.payment.user.model.MerchantUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantUserRepository extends JpaRepository<MerchantUser, Long> {

//    boolean existsByUserSecurityDetails_RoleName(String roleName);

    boolean existsById(Long id);

    Optional<MerchantUser> findByIdAndMerchant_MerchantId(Long id, String merchant_merchantId);

//    Page<MerchantUser> findByMerchant_MerchantId(String merchantId, Pageable pageable);
//
//    boolean existsByUserSecurityDetails_EmailAndUserSecurityDetails_Enabled(String email, boolean enabled);
//
//    @Query(value = "select email, role_id from merchant_user where email = ?1", nativeQuery = true)
//    MerchantUserAuth selectUserAuthDetails(String email);

    boolean existsByUserSecurityDetails_Email(String email);

    Optional<MerchantUser> findByUserSecurityDetails_Email(String email);
}
