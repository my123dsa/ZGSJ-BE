package com.example.core_bank.core_bank.core.repository;

import com.example.core_bank.core_bank.core.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("select a from Account a join fetch a.bank where a.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumberWithBank(String accountNumber);

    @Query("select a from Account a join fetch a.bank b " +
            "where a.accountNumber = :accountNumber " +
            "and b.bankCode= :bankCode and a.name= :name")
    Optional<Account> findByAccountNumberAndBankCodeAndName(
            @Param("accountNumber") String accountNumber,
            @Param("bankCode") String bankCode,
            @Param("name") String name);
}
