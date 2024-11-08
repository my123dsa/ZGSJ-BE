package com.example.User.repository;

import com.example.User.model.President;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PresidentRepository extends JpaRepository<President, Integer> {
    Optional<President> findByEmail(String email);

    boolean existsByEmailAndPhoneNumber(String email, String phoneNumber);

    void deleteByEmail(String email);

    //사장 정보 수정 쿼리 요청
    @Modifying
    @Transactional
    @Query("UPDATE President p SET p.phoneNumber = :phoneNumber, p.birthDate = :birthDate WHERE p.id = :id")
    int updatePhoneNumberAndBirthDate(@Param("id") Integer id,
                                      @Param("phoneNumber") String phoneNumber,
                                      @Param("birthDate") LocalDate birthDate);
}

