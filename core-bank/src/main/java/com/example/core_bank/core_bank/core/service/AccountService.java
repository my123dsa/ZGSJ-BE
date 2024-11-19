package com.example.core_bank.core_bank.core.service;

import com.example.core_bank.core_bank.core.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public boolean isAccountExists(String name, String accountNumber, String bankCode) {

        Optional<Integer>  _id= accountRepository.existsByAccountNumberAndNameAndBankCode(name, accountNumber, bankCode);

        if (_id.isPresent()) {
            return true;
        }
        return false;
    }
}
