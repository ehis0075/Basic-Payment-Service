package com.payment.permission.repository;

import com.payment.permission.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findByName(String name);

    boolean existsByName(String name);

    List<Permission> findBySuperAdminTrue();

    List<Permission> findByMerchantAdminTrue();

    Page<Permission> findBySuperAdminTrue(Pageable pageable);

    Page<Permission> findByMerchantAdminTrue(Pageable pageable);

}
