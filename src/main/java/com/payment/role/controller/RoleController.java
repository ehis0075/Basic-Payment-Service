package com.payment.role.controller;


import com.payment.general.dto.Response;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.general.service.GeneralService;
import com.payment.role.dto.CreateUpdateRoleDTO;
import com.payment.role.dto.RoleDTO;
import com.payment.role.service.RoleService;
import com.payment.merchant.service.MerchantService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/adminRoles")
public class RoleController {

    private final MerchantService merchantService;
    private final GeneralService generalService;
    private final RoleService adminRoleService;

    public RoleController(MerchantService merchantService, GeneralService generalService, RoleService adminRoleService) {
        this.merchantService = merchantService;
        this.generalService = generalService;
        this.adminRoleService = adminRoleService;
    }

    @PostMapping("/create")  // create Role API
    public Response createRole(@RequestBody CreateUpdateRoleDTO requestDTO) {

        String user = merchantService.getLoggedInUser();

        RoleDTO data = adminRoleService.addRole(requestDTO, user);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }


}
