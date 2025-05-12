package com.example.Attendance.repository;

import com.example.Attendance.dto.batch.CommuteSummary;
import com.example.Attendance.model.Commute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CommuteRepository extends JpaRepository<Commute, Integer> {

    @Modifying
    @Query("UPDATE Commute c " + "SET c.startTime = :startTime, " + "c.endTime = :endTime, " +
            "c.commuteDate = DATE(:commuteDate), " + "c.commuteDuration = :commuteDuration " + "WHERE c.id = :commuteId")
    void updateCommute(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime,
            @Param("commuteDate") LocalDate commuteDate, @Param("commuteDuration") Long commuteDuration,
            @Param("commuteId") Integer commuteId);

    @Query("SELECT c FROM Commute c " +
            "JOIN FETCH c.storeEmployee se " +
            "JOIN FETCH se.store s " +
            "WHERE s.id = :storeId " +
            "AND YEAR(c.commuteDate) = :year " +
            "AND MONTH(c.commuteDate) = :month " +
            "ORDER BY c.commuteDate, c.startTime")
    List<Commute> findMonthlyCommutesByStore(
            @Param("storeId") Integer storeId,
            @Param("year") int year,
            @Param("month") int month
    );

    Optional<Commute> findTopByStoreEmployeeIdOrderByStartTimeDesc(Integer seId);

    @Query("SELECT new com.example.Attendance.dto.batch.CommuteSummary(c.storeEmployee.id, sum(c.commuteDuration), " +
            "DATEDIFF" +
            "(max(c.commuteDate), min(c.commuteDate)) + 1) " +
            "FROM Commute c " +
            "WHERE DATE(c.commuteDate) BETWEEN DATE(:startDate) AND DATE(:endDate) " +
            "AND c.storeEmployee.id IN :employeeIds " +
            "GROUP BY c.storeEmployee.id "
    )
    List<CommuteSummary> findAllByCommuteDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("employeeIds") List<Integer> employeeIds);


    @Query("SELECT c FROM Commute c " +
            "JOIN FETCH c.storeEmployee se " +
            "WHERE se.store.id = :storeId " +
            "AND DATE(c.commuteDate) = DATE(:commuteDate)")
    List<Commute> findByStoreIdAndCommuteDate(
            @Param("storeId") Integer storeId,
            @Param("commuteDate") LocalDate commuteDate
    );
}
