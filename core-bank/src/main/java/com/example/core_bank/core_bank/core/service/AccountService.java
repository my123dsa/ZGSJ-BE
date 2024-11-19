package com.example.core_bank.core_bank.core.service;

import com.example.core_bank.core_bank.core.dto.accountnumber.AccountVerifyRequest;
import com.example.core_bank.core_bank.core.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public boolean isAccountExists(String name, String accountNumber, String bankCode) {
        return accountRepository.existsByNameAndAccountNumberAndBankCode(name, accountNumber, bankCode);
    }
}
