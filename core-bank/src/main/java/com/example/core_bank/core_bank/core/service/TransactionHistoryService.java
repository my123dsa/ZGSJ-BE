package com.example.core_bank.core_bank.core.service;

import com.example.core_bank.core_bank.core.dto.TransactionHistoryResponse;
import com.example.core_bank.core_bank.core.model.Account;
import com.example.core_bank.core_bank.core.model.TransactionHistory;
import com.example.core_bank.core_bank.core.repository.TransactionHistoryRepository;
import com.example.core_bank.core_bank.core.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionHistoryService {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final AccountRepository accountRepository;

    /**
     * 계좌 ID로 거래 내역을 조회하고, AccountResponseDto 리스트 반환
     */
//    public List<AccountResponseDto> getAccountsWithTransactionHistory(Integer accountId) {
//        List<TransactionHistory> transactionHistories = transactionHistoryRepository.findByAccountId(accountId);
//        return transactionHistories.stream()
//                .map(transactionHistory -> AccountResponseDto.from(transactionHistory.getAccount()))
//                .collect(Collectors.toList());
//    }
//
//    /**


//     * bankCode, accountNumber, depositor로 거래 내역 조회
//     */
    public List<TransactionHistoryResponse> getTransactionHistory(
            String bankCode,
            String accountNumber,
            Integer year,
            Integer month
    )
    {
        // 계좌 조회
        Optional<Account> accountOpt = accountRepository.findByAccountNumberWithBank(accountNumber);

        // 계좌가 존재하지 않거나 bankCode가 일치하지 않으면 빈 리스트 반환
        if (accountOpt.isEmpty() || !accountOpt.get().getBank().getBankCode().equals(bankCode)) {
            log.info("계좌 존재 x or bankcode");
            return List.of();
        }

        // 계좌가 존재하고 bankCode가 일치하는 경우, 거래 내역 조회
        Account account = accountOpt.get();
        List<TransactionHistory> transactionHistories = transactionHistoryRepository.findByAccountIdAndYearAndMonthWithClassfication(account.getId(), year, month);

        // 거래 내역을 TransactionHistoryResponse로 변환하여 반환
        return transactionHistories.stream()
                .map(TransactionHistoryResponse::from)
                .collect(Collectors.toList());
    }
}
