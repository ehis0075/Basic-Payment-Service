package com.payment.merchant.service.impl;

import com.payment.merchant.repository.MerchantRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class MerchantServiceImplTest {

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private MerchantServiceImpl merchantService;


}