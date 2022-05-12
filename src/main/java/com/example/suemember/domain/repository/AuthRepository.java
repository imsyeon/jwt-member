package com.example.suemember.domain.repository;

import com.example.suemember.domain.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findById(Long id);
    Optional<Auth> findByAuthId(Long authId);
}
