package com.payment.merchant.service;


import com.payment.general.dto.Response;
import com.payment.merchant.dto.AdminUserDTO;
import com.payment.merchant.dto.CreateUpdateUserDTO;
import com.payment.merchant.model.Merchant;

public interface MerchantService {
    Response signIn(String username, String password);

    String getLoggedInUser();

    AdminUserDTO addUser(CreateUpdateUserDTO createAdminUserDto);

    AdminUserDTO getUserDTO(Merchant merchant);


    Merchant get(String username);
}
