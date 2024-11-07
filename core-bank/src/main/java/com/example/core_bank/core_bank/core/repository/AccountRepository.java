package com.example.core_bank.core_bank.core.repository;

import com.example.core_bank.core_bank.core.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByAccountNumber(String accountNumber);
}
