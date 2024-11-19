package com.example.core_bank.core_bank.core.repository;

import com.example.core_bank.core_bank.core.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("select a from Account a join fetch a.bank where a.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumberWithBank(String accountNumber);


    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Account a " +
            "WHERE EXISTS (SELECT 1 FROM Bank b WHERE b.id = a.bank.id AND b.bankCode = :bankCode) " +
            "AND a.name = :name AND a.accountNumber = :accountNumber")
    boolean existsByAccountNumberAndNameAndBankCode(String name, String accountNumber, String bankCode);

}
