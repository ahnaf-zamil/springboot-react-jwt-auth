package com.devguyahnaf.springbootauth.repositories;

import com.devguyahnaf.springbootauth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Component
@Repository
public interface AuthRepository extends JpaRepository<User, Long> {
    Optional<User> getByEmail(String email);
}
