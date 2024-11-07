package com.example.core_bank.core_bank.core.service;

import com.example.core_bank.core_bank.core.dto.AccountResponseDto;
import com.example.core_bank.core_bank.core.model.Account;
import com.example.core_bank.core_bank.core.model.TransactionHistory;
import com.example.core_bank.core_bank.core.repository.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionHistoryService {

    private final TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    public TransactionHistoryService(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public List<TransactionHistory> getTransactionHistoryByAccountId(Integer accountId) {
        return transactionHistoryRepository.findByAccountId(accountId);
    }

    public List<AccountResponseDto> getAccountsWithTransactionHistory(Integer accountId) {
        List<TransactionHistory> transactionHistories = getTransactionHistoryByAccountId(accountId);

        // AccountResponseDto로 변환
        return transactionHistories.stream()
                .map(transactionHistory -> AccountResponseDto.from(transactionHistory.getAccount()))
                .collect(Collectors.toList());
    }
}
