package com.payment.merchant.service.implementation;

import com.payment.exception.GeneralException;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.merchant.dto.CreateMerchantDTO;
import com.payment.merchant.dto.MerchantDTO;
import com.payment.merchant.model.Merchant;
import com.payment.merchant.repository.MerchantRepository;
import com.payment.merchant.service.MerchantService;
import com.payment.user.service.MerchantUserService;
import com.payment.util.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {

    private final MerchantUserService userService;
    private final MerchantRepository merchantRepository;

    public MerchantServiceImpl(MerchantUserService userService,
                               MerchantRepository merchantRepository) {
        this.userService = userService;
        this.merchantRepository = merchantRepository;
    }

    @Override
    public Merchant findByMerchantId(String merchantId) {
        return getMerchant(merchantRepository.findByMerchantId(merchantId));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public MerchantDTO createMerchant(CreateMerchantDTO merchantDTO) {
        log.info("Creating merchant account with payload {}", merchantDTO);

        String name = merchantDTO.getBusinessName();
        //validate that merchant name is not empty
        if (GeneralUtil.stringIsNullOrEmpty(name)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode,
                    "Business name cannot be empty");
        }

        //validate that merchant name does not already exist
        validateThatMerchantNameDoesNotExist(name);

        //validate the merchant user does not exist
        userService.validateThatUserDoesNotExist(merchantDTO.getEmail());

        log.info("All validation was successful, proceeding to creation");

        //create new merchant entry
        Merchant merchant = new Merchant();
        merchant.setName(merchantDTO.getBusinessName());

        //generated merchant ID
        merchant.setMerchantId(generateMerchantId(name));


        merchant = merchantRepository.save(merchant);

        //create admin user account
        userService.addFirstMerchantUser(merchantDTO, merchant);

        // update merchant account
        merchant = merchantRepository.saveAndFlush(merchant);

        return Merchant.getMerchantDTO(merchant);
    }

    private Merchant getMerchant(Merchant byMerchantId) {
        return Optional.ofNullable(byMerchantId)
                .orElseThrow(() -> new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88.responseCode,
                        "Merchant not found"));
    }

    private void validateThatMerchantNameDoesNotExist(String name) {
        log.info(name);
        boolean merchantExist = merchantRepository.existsByName(name);

        log.info("boolean value {}", merchantExist);

        if (merchantExist) {
            log.info("merchant name {} already exist", name);
            throw new GeneralException(ResponseCodeAndMessage.ALREADY_EXIST_86.responseCode,
                    "Business Name already exist");
        }

    }


    static String generateMerchantId(String merchantName) {
        log.info("Generating merchant Id for {}", merchantName);

        merchantName = merchantName.replace(" ", "").toUpperCase(Locale.ROOT);

        String merchantId;
        if (merchantName.length() > 4) {
            merchantId = merchantName.substring(0, 4) + generateRandomNumbers().substring(0, 6);
        } else {
            merchantId = "XP" + merchantName + generateRandomNumbers().substring(0, 10 - merchantName.length());
        }

        //check if merchantId is available
        return merchantId;
    }

    static String generateRandomNumbers() {
        double a = Math.random();
        return Double.toString(a).split("\\.")[1];
    }

}
