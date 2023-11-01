package com.payment.user.service;



import com.payment.auth.dto.UserAndMerchantIdResponse;
import com.payment.merchant.dto.CreateMerchantDTO;
import com.payment.merchant.model.Merchant;
import com.payment.permission.enums.PermissionType;
import com.payment.permission.model.Permission;
import com.payment.user.dto.CreateMerchantUserDTO;
import com.payment.user.dto.MerchantUserDTO;
import com.payment.user.dto.MerchantUserDTO2;
import com.payment.user.model.MerchantUser;

import java.util.List;

public interface MerchantUserService {
    MerchantUserDTO addUser(CreateMerchantUserDTO createUserDto);

    List<MerchantUser> getAll();

    void addFirstMerchantUser(CreateMerchantDTO merchantDTO, Merchant merchant);

    List<Permission> getPermissionsByPermissionType(PermissionType permissionType);

    void validateThatUserDoesNotExist(String email);

    MerchantUserDTO getUserDTO(MerchantUser user);

    MerchantUser saveUser(MerchantUser user);

    UserAndMerchantIdResponse getMerchantUserIdAndMerchantIdByMerchantEmail(String email);

}
