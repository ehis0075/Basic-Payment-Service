package com.payment.permission.model;

import com.payment.permission.enums.PermissionType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Data
@Entity(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean merchantAdmin = false;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean superAdmin = true;

    public void setPermissionType(PermissionType permissionType) {
        this.merchantAdmin = permissionType.equals(PermissionType.MERCHANT);
        this.superAdmin = true;
    }

    public PermissionType getPermissionType() {
        return this.merchantAdmin ? PermissionType.MERCHANT : PermissionType.SUPER;
    }
}