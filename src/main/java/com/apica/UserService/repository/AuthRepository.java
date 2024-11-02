package com.apica.UserService.repository;

import com.apica.UserService.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<Auth, UUID> {
    Optional<Auth> findByEmail(String email);

    void deleteByEmail(String email);
}
