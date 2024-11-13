package com.example.Attendance.repository;

import com.example.Attendance.model.Commute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommuteRepository extends JpaRepository<Commute, Integer> {
    @Modifying
    @Query("UPDATE Commute c " + "SET c.startTime = :startTime, " + "c.endTime = :endTime, " +
            "c.commuteDate = :commuteDate, " + "c.commuteDuration = :commuteDuration " + "WHERE c.id = :commuteId")
    void updateCommute(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("commuteDate") LocalDate commuteDate,
            @Param("commuteDuration") Long commuteDuration, @Param("commuteId") Integer commuteId);

    @Query("SELECT c FROM Commute c " +
            "JOIN FETCH c.storeEmployee se " +
            "JOIN FETCH se.store s " +
            "WHERE s.id = :storeId " +
            "AND YEAR(c.commuteDate) = :year " +
            "AND MONTH(c.commuteDate) = :month " +
            "ORDER BY c.commuteDate, se.name")
    List<Commute> findMonthlyCommutesByStore(
            @Param("storeId") Integer storeId,
            @Param("year") int year,
            @Param("month") int month
    );

    Optional<Commute> findTopByStoreEmployeeIdOrderByStartTimeDesc(Integer seId);
}
