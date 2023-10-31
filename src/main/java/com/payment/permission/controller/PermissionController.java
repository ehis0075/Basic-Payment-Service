package com.payment.permission.controller;

import com.payment.general.dto.Response;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.general.service.GeneralService;
import com.payment.permission.dto.PermissionListDTO;
import com.payment.permission.dto.PermissionRequestDTO;
import com.payment.permission.service.PermissionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    private final GeneralService generalService;
    private final PermissionService permissionService;

    public PermissionController(GeneralService generalService, PermissionService permissionService) {
        this.generalService = generalService;
        this.permissionService = permissionService;
    }

    @PostMapping()
    public Response getAllPermission(@RequestBody PermissionRequestDTO requestDTO) {
        PermissionListDTO data = permissionService.getAllPermission(requestDTO);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

}
