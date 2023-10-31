package com.payment.merchant.model;


import com.payment.role.model.Role;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private String merchantId;

    private String merchantBusinessName;

    @ManyToOne
    @ToString.Exclude
    private Role userRole;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Merchant)) return false;
        Merchant that = (Merchant) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

//    public static AdminUserDTO getUserDTO(AdminUser adminUser) {
//
//        AdminUserDTO adminUserDTO = new AdminUserDTO();
//
//        BeanUtils.copyProperties(adminUser, adminUserDTO);
//
//        //get role dto
//        RoleDTO roleDTO = Role.getAdminRoleDTO(adminUser.getUserRole());
//        adminUserDTO.setRole(roleDTO);
//
//        return adminUserDTO;
//    }
}
