package com.example.core_bank.core_bank.core.service;

import com.example.core_bank.core_bank.core.dto.transfer.TransferRequest;
import com.example.core_bank.core_bank.core.model.Account;
import com.example.core_bank.core_bank.core.model.Classfication;
import com.example.core_bank.core_bank.core.model.TransactionHistory;
import com.example.core_bank.core_bank.core.repository.AccountRepository;
import com.example.core_bank.core_bank.core.repository.TransactionHistoryRepository;
import com.example.core_bank.core_bank.global.error.CustomException;
import com.example.core_bank.core_bank.global.error.ErrorCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankCoreService {

    private final AccountRepository accountRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final EntityManager entityManager;

    @Transactional
    public LocalDate transfer(TransferRequest transferRequest) {

        // 리스트 정렬해서 받음.
        // 여기 로직 더 효율적이게 변경 되어야함
        Account fromAccount = accountRepository.findByAccountNumberAndBankCodeAndName(
                        transferRequest.getFromAccount(), transferRequest.getFromBankCode(),
                        transferRequest.getFromAccountDepositor())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        Account toAccount = accountRepository.findByAccountNumberAndBankCodeAndName(
                        transferRequest.getToAccount(), transferRequest.getToBankCode(),
                        transferRequest.getToAccountDepositor())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        LocalDate now = LocalDate.now();

        List<Account> processedAccounts = processTransfer(fromAccount, toAccount, transferRequest.getAmount());
        accountRepository.saveAll(processedAccounts);

        List<TransactionHistory> histories = createHistories(fromAccount, toAccount,now,transferRequest.getAmount());
        transactionHistoryRepository.saveAll(histories);
        return now;
    }

    public List<TransactionHistory> createHistories(Account fromAccount, Account toAccount,LocalDate now,Long amount) {
        Classfication transfer = entityManager.getReference(Classfication.class, 3);
        Classfication deposit = entityManager.getReference(Classfication.class, 26);
        return Arrays.asList(TransactionHistory.createTransactionHistory
                        (amount,now, false, "이체", fromAccount, transfer),
                TransactionHistory.createTransactionHistory
                        (amount,now, true, "입금", toAccount, deposit));
    }

    public List<Account> processTransfer(Account fromAccount, Account toAccount, Long amount) {
        if (fromAccount.getBalance() < amount) {
            throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
        }
        fromAccount.transfer(amount);
        toAccount.deposit(amount);
        return Arrays.asList(fromAccount, toAccount);
    }
}
