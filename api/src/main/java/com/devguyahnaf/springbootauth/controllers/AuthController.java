package com.devguyahnaf.springbootauth.controllers;

import com.devguyahnaf.springbootauth.exceptions.UserExists;
import com.devguyahnaf.springbootauth.models.User;
import com.devguyahnaf.springbootauth.repositories.AuthRepository;
import com.devguyahnaf.springbootauth.services.AuthService;
import com.devguyahnaf.springbootauth.services.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;


@AllArgsConstructor
@Getter
class LoginRequest {
    private Optional<String> email;
    private Optional<String> password;
}

@AllArgsConstructor
@Getter
class RegisterRequest {
    private Optional<String> name;
    private Optional<String> email;
    private Optional<String> password;
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
class RefreshTokenRequest {
    private Optional<String> refreshToken;
}

@RestController
@RequestMapping(path = "/auth/")
public class AuthController {
    @Autowired
    public BCryptPasswordEncoder encoder;
    @Autowired
    private AuthRepository authRepository;
    @Value("${auth.jwt.access-token-secret}")
    private String accessTokenSecret;

    @Value("${auth.jwt.refresh-token-secret}")
    private String refreshTokenSecret;

    @Autowired
    private AuthService authService;

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerAccount(@RequestBody RegisterRequest payload) {
        if (!payload.getName().isPresent() || !payload.getEmail().isPresent() || !payload.getPassword().isPresent()) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
        if (!isValidEmailAddress(payload.getEmail().get())) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }

        String name = payload.getName().get();
        String email = payload.getEmail().get();
        String password = payload.getPassword().get();

        try {
            User newUser = authService.registerUser(name, email, encoder.encode(password));
            JWTService jwtService = new JWTService(newUser, accessTokenSecret, refreshTokenSecret);

            String accessToken = jwtService.generateAccessToken();
            String refreshToken = jwtService.generateRefreshToken();

            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("accessToken", accessToken);
            responseMap.put("refreshToken", refreshToken);
            return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
        } catch (UserExists e) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest payload) {
        if (!payload.getEmail().isPresent() || !payload.getPassword().isPresent()) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }

        String email = payload.getEmail().get();
        String password = payload.getPassword().get();

        Optional<User> user = authService.getUser(email);
        if (!user.isPresent()) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        if (!encoder.matches(password, user.get().getPassword())) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        JWTService jwtService = new JWTService(user.get(), accessTokenSecret, refreshTokenSecret);

        String accessToken = jwtService.generateAccessToken();
        String refreshToken = jwtService.generateRefreshToken();

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("accessToken", accessToken);
        responseMap.put("refreshToken", refreshToken);

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<Object> getRefreshToken(@RequestBody RefreshTokenRequest request, HttpServletResponse response) {
        if (!request.getRefreshToken().isPresent()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.BAD_REQUEST);
        }

        JWTService jwtService = new JWTService(new User(), accessTokenSecret, refreshTokenSecret);
        try {
            long userId = jwtService.getUserId(request.getRefreshToken().get(), refreshTokenSecret);
            User user = authRepository.getById(userId);

            jwtService = new JWTService(user, accessTokenSecret, refreshTokenSecret);

            String accessToken = jwtService.generateAccessToken();
            String refreshToken = jwtService.generateRefreshToken();

            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("accessToken", accessToken);
            responseMap.put("refreshToken", refreshToken);

            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

    }

}
