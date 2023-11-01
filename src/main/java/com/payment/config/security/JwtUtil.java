package com.payment.config.security;

import com.payment.auth.dto.UserDetailsPayloadDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
@Getter
@Slf4j
public class JwtUtil {

    //@Value("${jwt.secret}")
    private final String jwtSecret = "xpre55P@ym3";

    public String generateToken(Authentication authentication){
        log.info("Generating token .....");

        UserDetailsPayloadDTO userDetailsPayloadDTO = (UserDetailsPayloadDTO) authentication.getPrincipal();

        return Jwts.builder().setIssuer("Xpress Payments")
                .setSubject(userDetailsPayloadDTO.getEmail())
                .claim("email", userDetailsPayloadDTO.getEmail())
                .claim("userId", userDetailsPayloadDTO.getUserId())
                .claim("userType", userDetailsPayloadDTO.getUserType())
                .claim("authorities", userDetailsPayloadDTO.getAuthorities())
                .claim("merchantId", userDetailsPayloadDTO.getMerchantId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 3600000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

}
