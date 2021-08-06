package com.devguyahnaf.springbootauth.services;

import com.devguyahnaf.springbootauth.models.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;

@RequiredArgsConstructor
public class JWTService {
    private final User user;

    private final String accessTokenSecret;

    private final String refreshTokenSecret;

    public String generateAccessToken() {
        Calendar now = Calendar.getInstance();
        Calendar _now = Calendar.getInstance();
        _now.add(Calendar.MINUTE, 15);
        return Jwts.builder()
                .setIssuer("DevGuyAhnaf")
                .setSubject(user.getId().toString())
                .setIssuedAt(now.getTime())
                .setExpiration(_now.getTime())
                .signWith(SignatureAlgorithm.HS256, accessTokenSecret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public String generateRefreshToken() {
        Calendar now = Calendar.getInstance();
        Calendar _now = Calendar.getInstance();
        _now.add(Calendar.MONTH, 1);
        return Jwts.builder()
                .setIssuer("DevGuyAhnaf")
                .setSubject(user.getId().toString())
                .setIssuedAt(now.getTime())
                .setExpiration(_now.getTime())
                .signWith(SignatureAlgorithm.HS256, refreshTokenSecret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public long getUserId(String jwt, String secret) throws ExpiredJwtException {
        return Long.parseLong(Jwts.parserBuilder()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(jwt).getBody().getSubject());
    }
}
