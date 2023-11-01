package com.payment.role.controller;

import com.payment.general.dto.Response;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.general.service.GeneralService;
import com.payment.role.dto.CreateUpdateMerchantRoleDTO;
import com.payment.role.dto.MerchantRoleDTO;
import com.payment.role.service.MerchantRoleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/merchantRoles")
public class MerchantRoleController {

    private final MerchantRoleService merchantRoleService;
    private final GeneralService generalService;

    public MerchantRoleController(MerchantRoleService merchantRoleService, GeneralService generalService) {
        this.merchantRoleService = merchantRoleService;
        this.generalService = generalService;
    }

    @PostMapping("/create")
    public Response createRole(@RequestBody CreateUpdateMerchantRoleDTO requestDTO) {

        MerchantRoleDTO data = merchantRoleService.addRole(requestDTO);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

}
