package com.payment.user.model;

import com.payment.auth.model.UserSecurityDetails;
import com.payment.merchant.model.Merchant;
import com.payment.role.model.MerchantRole;
import lombok.*;

import javax.persistence.*;

@Data
@Entity(name = "merchant_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Merchant merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private MerchantRole role;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserSecurityDetails userSecurityDetails;

}
