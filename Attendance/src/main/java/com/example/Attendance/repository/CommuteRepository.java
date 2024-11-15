package com.example.Attendance.repository;

import com.example.Attendance.dto.CommuteSummary;
import com.example.Attendance.model.Commute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommuteRepository extends JpaRepository<Commute, Integer> {


    Optional<Commute> findTopByStoreEmployeeIdOrderByStartTimeDesc(Integer seId);

    @Query("SELECT new com.example.Attendance.dto.CommuteSummary(c.storeEmployee.id, sum(c.commuteDuration)) " +
            "FROM Commute c " +
            "WHERE c.commuteDate BETWEEN :startDate AND :endDate AND c.storeEmployee.id IN :employeeIds " +
            "GROUP BY c.storeEmployee.id"
    )
    List<CommuteSummary> findAllByCommuteDateBetween(
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate,
            @Param("employeeIds") List<Integer> employeeIds);
}
