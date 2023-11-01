package com.payment.merchant.model;

import com.payment.merchant.dto.MerchantDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Slf4j
@Data
@Entity(name = "merchant")
@ToString
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 16)
    private String merchantId; //generated random 16 digits

    @Column(unique = true, length = 50)
    private String name;

    public static MerchantDTO getMerchantDTO(Merchant merchant) {
        MerchantDTO merchantDTO = new MerchantDTO();
        BeanUtils.copyProperties(merchant, merchantDTO);

        return merchantDTO;
    }

}
