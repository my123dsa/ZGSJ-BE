package com.example.User.repository;

import com.example.User.model.President;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresidentRepository extends JpaRepository<President, Integer> {
}
