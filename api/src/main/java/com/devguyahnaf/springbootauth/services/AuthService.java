package com.devguyahnaf.springbootauth.services;

import com.devguyahnaf.springbootauth.exceptions.UserExists;
import com.devguyahnaf.springbootauth.models.User;
import com.devguyahnaf.springbootauth.models.generator.Snowflake;
import com.devguyahnaf.springbootauth.repositories.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    public Snowflake snowflake;

    @Autowired
    private AuthRepository authRepository;

    public User registerUser(String name, String email, String password) throws UserExists {
        Optional<User> user = authRepository.getByEmail(email);
        if (user.isPresent()) {
            throw new UserExists("User with email '" + email + "' already exists.");
        }
        User newUser = new User(snowflake.nextId(), name, email, password);
        return authRepository.save(newUser);
    }

    public Optional<User> getUser(String email) {
        return authRepository.getByEmail(email);
    }
}
