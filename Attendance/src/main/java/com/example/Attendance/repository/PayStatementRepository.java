package com.example.Attendance.repository;

import com.example.Attendance.dto.PayStatementResponse;
import com.example.Attendance.model.PayStatement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PayStatementRepository extends CrudRepository<PayStatement, Integer> {

    @Query("SELECT " +
            "  new com.example.Attendance.dto.PayStatementResponse(" +
            "    ps.id, " +
            "    se.bankCode, " +
            "    se.accountNumber, " +
            "    ps.amount, " +
            "    ps.issuanceDate" +
            "  ) " +
            "FROM PayStatement ps " +
            "INNER JOIN StoreEmployee se ON ps.seId = se.id " +
            "WHERE se.store.id = :storeId " +
            "AND FUNCTION('YEAR', ps.issuanceDate) = :year " +
            "AND FUNCTION('MONTH', ps.issuanceDate) = :month")
    List<PayStatementResponse> findPayStatementResponsesByStoreAndDateWithFetch(
            @Param("storeId") Integer storeId,
            @Param("year") Integer year,
            @Param("month") Integer month
    );

}
