package com.payment.config.security;


import com.payment.auth.dto.UserAndMerchantIdResponse;
import com.payment.auth.dto.UserDetailsPayloadDTO;
import com.payment.auth.model.UserSecurityDetails;
import com.payment.auth.repository.UserSecurityDetailsRepository;
import com.payment.exception.GeneralException;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.user.service.MerchantUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserSecurityDetailsRepository userSecurityDetailsRepository;
    private MerchantUserService merchantUserService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("inside load user by username method----->");
        try{
            UserSecurityDetails userSecurityDetails = userSecurityDetailsRepository.findByEmail(username).orElseThrow(()-> new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88.responseCode, "User does not exist"));
            log.info("security details {}", userSecurityDetails);

            UserAndMerchantIdResponse userAndMerchantIdResponse = getUserAndMerchantIdResponse(userSecurityDetails.getUserType(), userSecurityDetails.getEmail());
            return getUserDetailsPayload(userSecurityDetails, userAndMerchantIdResponse);
        }catch (Exception e){
            throw new UsernameNotFoundException("User does not exist");
        }
    }

    private UserDetailsPayloadDTO getUserDetailsPayload(UserSecurityDetails userSecurityDetails, UserAndMerchantIdResponse userAndMerchantIdResponse){
       return new UserDetailsPayloadDTO(userSecurityDetails.getEmail(), userSecurityDetails.getPassword(), userSecurityDetails.getUserType(), userSecurityDetails.isEnabled(), userSecurityDetails.isLocked(), userSecurityDetails.getRoleName(), (Set<? extends GrantedAuthority>) userSecurityDetails.getAuthorities(),userAndMerchantIdResponse.getId(), userAndMerchantIdResponse.getMerchantId(), getPermissions(userSecurityDetails.getAuthorities()));
    }
    private UserAndMerchantIdResponse getUserAndMerchantIdResponse(String userType, String email){
        return merchantUserService.getMerchantUserIdAndMerchantIdByMerchantEmail(email);
    }

    private Set<String> getPermissions(Collection<? extends GrantedAuthority> authorities){
        return authorities.stream().map(String::valueOf).collect(Collectors.toSet());
    }
}
