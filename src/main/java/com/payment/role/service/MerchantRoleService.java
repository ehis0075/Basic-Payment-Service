package com.payment.role.service;


import com.payment.merchant.model.Merchant;
import com.payment.permission.model.Permission;
import com.payment.role.dto.CreateUpdateMerchantRoleDTO;
import com.payment.role.dto.MerchantRoleDTO;
import com.payment.role.model.MerchantRole;

import java.util.List;

public interface MerchantRoleService {

    MerchantRoleDTO addRole(CreateUpdateMerchantRoleDTO roleDTO);

    MerchantRole addRole(MerchantRole role, String name, Merchant merchant, List<Permission> permissions);

    MerchantRoleDTO getRoleDTO(MerchantRole role);

    MerchantRoleDTO getRoleById(Long roleId);

    MerchantRole getMerchantRoleById(Long roleId);

}
