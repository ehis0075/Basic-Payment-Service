package com.payment.merchant.repository;



import com.payment.merchant.imodel.UserBasicInfoI;
import com.payment.merchant.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Merchant findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByMerchantId(String merchantId);

    @Query(value = "select au.id, au.email, au.phone_number, au.first_name, au.last_name from admin_user au\n" +
            "    inner join roles_permissions arp on au.role_id = arp.role_id\n" +
            "    inner join permission p on p.id = arp.permission_id\n" +
            "where p.name = ?1 AND au.enabled = 1", nativeQuery = true)
    List<UserBasicInfoI> getUsersWithPermission(String permissionName);


}
