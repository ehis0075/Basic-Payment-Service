package com.payment.merchant.dto;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
public class CreateUpdateUserDTO {

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private Long roleId;

    private String password;

    private String bizName;
}
