package com.example.Attendance.repository;

import com.example.Attendance.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Integer> {

    List<Batch> findAllByIssuanceDate(LocalDate issuanceDate);

    @Modifying
    @Query("UPDATE Batch b SET b.emailResult = true WHERE b.id IN :ids")
    void updateEmailResultByIds(@Param("ids") List<Integer> ids);

    @Modifying
    @Query("UPDATE Batch b SET b.bankResult = true WHERE b.id IN :ids")
    void updateBankResultByIds(@Param("ids") List<Integer> ids);

    @Modifying
    @Query("UPDATE Batch b SET b.pdfResult = true WHERE b.id IN :ids")
    void updatePdfResultByIds(@Param("ids") List<Integer> ids);

    @Modifying
    @Query("UPDATE Batch b SET b.pdfResult = true, b.url = :url WHERE b.id = :id")
    void updatePdfResultAndUrl(@Param("id") Integer id, @Param("url") String url);
}
