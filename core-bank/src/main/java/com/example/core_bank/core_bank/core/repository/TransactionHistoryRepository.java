package com.example.core_bank.core_bank.core.repository;

import com.example.core_bank.core_bank.core.model.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer> {

    // fetch join
    @Query("SELECT th FROM TransactionHistory th " +
            "JOIN FETCH th.classfication " +
            "WHERE th.account.id = :accountId " +
            "AND YEAR(th.transactionDate) = :year " +
            "AND MONTH(th.transactionDate) = :month")
    List<TransactionHistory> findByAccountIdAndYearAndMonthWithClassfication(
            @Param("accountId") Integer accountId,
            @Param("year") Integer year,
            @Param("month") Integer month
    );
}
