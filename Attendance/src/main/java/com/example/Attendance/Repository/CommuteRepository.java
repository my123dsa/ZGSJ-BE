package com.example.Attendance.Repository;

import com.example.Attendance.model.Commute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommuteRepository extends JpaRepository<Commute, Integer> {

    Optional<Commute> findTopById(Integer id);
}
