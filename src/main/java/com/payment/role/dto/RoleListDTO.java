package com.payment.role.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.payment.general.dto.PageableResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleListDTO extends PageableResponseDTO {

    @JsonProperty("roles")
    private List<RoleDTO> roleDTOList;
}
