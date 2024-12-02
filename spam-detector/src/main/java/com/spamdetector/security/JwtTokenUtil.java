package com.spamdetector.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    // Generate token
    public String generateToken(String phoneNumber) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .setSubject(phoneNumber)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // Get phone number from token
    public String getPhoneNumberFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // Validate token
    public boolean validateToken(String token, String phoneNumber) {
        String tokenPhoneNumber = getPhoneNumberFromToken(token);
        return (tokenPhoneNumber.equals(phoneNumber) && !isTokenExpired(token));
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        Date expirationDate = getClaimsFromToken(token).getExpiration();
        return expirationDate.before(new Date());
    }

    // Extract claims from the token
    private Claims getClaimsFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
