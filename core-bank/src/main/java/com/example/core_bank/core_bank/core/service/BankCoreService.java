package com.example.core_bank.core_bank.core.service;

import com.example.core_bank.core_bank.core.dto.transfer.TransferRequest;
import com.example.core_bank.core_bank.core.model.Account;
import com.example.core_bank.core_bank.core.repository.AccountRepository;
import com.example.core_bank.core_bank.core.repository.TransactionHistoryRepository;
import com.example.core_bank.core_bank.global.error.CustomException;
import com.example.core_bank.core_bank.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BankCoreService {

    private final AccountRepository accountRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    @Transactional
    public LocalDate transfer(TransferRequest transferRequest) {

        // 리스트 정렬해서 받음.
        // 여기 로직 더 효율적이게 변경 되어야함
        Account fromAccount = findFromAccount(transferRequest);
        Account toAccount = findToAccount(transferRequest);

        validateBalance(fromAccount, transferRequest.getAmount());

        LocalDate now = LocalDate.now();

        processTransfer(fromAccount, toAccount, transferRequest.getAmount(), now);

        return now;
    }

    private void processTransfer(Account fromAccount, Account toAccount, Long amount, LocalDate now){
        Long fromResult=fromAccount.getBalance() -amount;
        Long toResult=toAccount.getBalance() + amount;
        accountRepository.updateBalances(fromAccount.getId(),toAccount.getId(),fromResult,toResult);

        transactionHistoryRepository.saveTransactionHistories(fromAccount.getId(),toAccount.getId(),
                amount,3,26,now);
    }

    private void validateBalance(Account account, Long amount) {
        if (account.getBalance() < amount) {
            throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
        }
    }

    private Account findAccount(String accountNumber, String bankCode, String depositorName) {
        return accountRepository.findByAccountNumberAndBankCodeAndName(
                        accountNumber,
                        bankCode,
                        depositorName)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    private Account findFromAccount(TransferRequest request){
        return findAccount(
                request.getFromAccount(),
                request.getFromBankCode(),
                request.getFromAccountDepositor()
        );
    }

    private Account findToAccount(TransferRequest request) {
        return findAccount(
                request.getToAccount(),
                request.getToBankCode(),
                request.getToAccountDepositor()
        );
    }
}
