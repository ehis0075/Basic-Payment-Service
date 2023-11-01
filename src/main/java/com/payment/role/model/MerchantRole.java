package com.payment.role.model;


import com.payment.merchant.model.Merchant;
import com.payment.permission.model.Permission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class MerchantRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "permission_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<Permission> permissions;

    public void setPermissions(List<Permission> permissions) {
        this.permissions = new HashSet<>(permissions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MerchantRole merchantRole = (MerchantRole) o;
        return id != null && Objects.equals(id, merchantRole.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}