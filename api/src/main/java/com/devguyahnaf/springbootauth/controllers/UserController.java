package com.devguyahnaf.springbootauth.controllers;

import com.devguyahnaf.springbootauth.models.User;
import com.devguyahnaf.springbootauth.repositories.AuthRepository;
import com.devguyahnaf.springbootauth.services.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users/")
public class UserController {
    @Autowired
    private AuthRepository authRepository;

    @Value("${auth.jwt.access-token-secret}")
    private String accessTokenSecret;

    @Value("${auth.jwt.refresh-token-secret}")
    private String refreshTokenSecret;

    @GetMapping("/@me")
    public ResponseEntity<Object> getCurrentUser(@RequestHeader("Authorization") String token) {
        JWTService jwtService = new JWTService(new User(), accessTokenSecret, refreshTokenSecret);
        try {
            long userId = jwtService.getUserId(token, accessTokenSecret);
            User user = authRepository.getById(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

    }
}
