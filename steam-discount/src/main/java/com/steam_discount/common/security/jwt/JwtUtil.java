package com.steam_discount.common.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtUtil {
    @Value("${jwt.access.expiration}")
    private long accessExpiration;
    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;
    @Value("${jwt.refresh.cookie.max.age}")
    private int refreshCookieMaxAge;

    private final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private final String ACCESS_TOKEN_HEADER_NAME = "Authorization";

    private final SecretKey accessKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final SecretKey refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateAccessToken(String email){
        return generateToken(email, accessKey, accessExpiration);
    }

    public String generateRefreshToken(String email){
        return generateToken(email, refreshKey, refreshExpiration);
    }

    private String generateToken(String email, SecretKey secretKey, long expireTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expireTime);

        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact();
    }

    public String getEmailFromAccessToken(String token){
        return getEmailFromToken(token, accessKey);
    }

    public String getEmailFromRefreshToken(String token){
        return getEmailFromToken(token, refreshKey);
    }

    private String getEmailFromToken(String token, SecretKey secretKey) {
        return getClaimsFromToken(token, secretKey).getSubject();
    }

    private Claims getClaimsFromToken(String token, SecretKey secretKey){
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    public boolean validateAccessToken(String token){
        return validateToken(token, accessKey);
    }

    public boolean validateRefreshToken(String token){
        return validateToken(token, refreshKey);
    }

    public boolean validateToken(String token, SecretKey secretKey) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}