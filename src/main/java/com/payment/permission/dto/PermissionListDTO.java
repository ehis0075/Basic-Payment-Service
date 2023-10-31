package com.payment.permission.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.payment.general.dto.PageableResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionListDTO extends PageableResponseDTO {

    @JsonProperty("permissions")
    private List<PermissionDTO> permissionDTOList;
}
