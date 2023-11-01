package com.payment.role.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateUpdateMerchantRoleDTO {
    private String name;
    private String merchantId;
    private List<String> permissionNames;
}
