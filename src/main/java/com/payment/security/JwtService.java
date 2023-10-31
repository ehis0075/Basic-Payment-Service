package com.payment.security;


import com.payment.exception.GeneralException;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.role.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Service
@Slf4j
public class JwtService {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateToken(String email, Role userRole) {
        log.info("Request to Generate jwt token for user {}", email);

        //set claims
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("auth", userRole);

        //set validity
        Date now = new Date();
        Date validity = new Date(System.currentTimeMillis() + 1000 * 60 * 24); // Set the expiration date (24 hours from now)

        //sign jwt
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String extractToken(HttpServletRequest req) {
        //extract token from authorization header
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        log.info("Request to validate token");

        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {

            log.info("Expired or invalid JWT token");
            throw new GeneralException(ResponseCodeAndMessage.UNAUTHORIZED_97.responseCode, "Expired or invalid JWT token!");
        }

    }
}
