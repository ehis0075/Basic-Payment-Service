package com.payment.merchant.service;


import com.payment.merchant.dto.CreateMerchantDTO;
import com.payment.merchant.dto.MerchantDTO;
import com.payment.merchant.model.Merchant;


public interface MerchantService {

    Merchant findByMerchantId(String merchantId);

    MerchantDTO createMerchant(CreateMerchantDTO merchantDTO);

}
