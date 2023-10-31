package com.payment.role.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateUpdateRoleDTO {
    private String name;
    private List<String> permissionNames;

}
