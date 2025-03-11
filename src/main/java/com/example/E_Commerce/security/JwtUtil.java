package com.example.E_Commerce.security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.E_Commerce.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long EXPIRATION_TIME = 259200000; 

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) 
                .claim("name", user.getName()) 
                .claim("role", user.getRole().toString()) 
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token, String email) {
        return email.equals(extractEmail(token)) && !isTokenExpired(token);
    }
    
    public boolean validateToken(String token) {
        String email = extractEmail(token);
        return validateToken(token, email);
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
    
    public String extractName(String token) {
        return getClaims(token).get("name", String.class);
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }


    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
