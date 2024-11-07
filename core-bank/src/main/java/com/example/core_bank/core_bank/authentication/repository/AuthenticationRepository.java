package com.example.core_bank.core_bank.authentication.repository;

import com.example.core_bank.core_bank.authentication.model.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Integer> {
    Optional<Authentication> findByEmail(String email);
}
