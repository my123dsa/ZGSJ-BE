package com.example.core_bank.core_bank.core.dto;

import com.example.core_bank.core_bank.core.model.TransactionHistory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionHistoryResponse {

    private LocalDate transactionDate;  // 거래 날짜
    private int amount;  // 거래 금액
    private boolean isDeposit;  // 입금 여부
    private String transactionType;  // 거래 종류
    private Integer classificationCode;  // 거래 분류 코드

    // 생성자
    public static TransactionHistoryResponse of(TransactionHistory transactionHistory) {
        // 날짜 형식 변환
        return new TransactionHistoryResponse(transactionHistory.getTransactionDate(),
                transactionHistory.getAmount(),
                transactionHistory.getIsDeposit(),
                transactionHistory.getTransactionType(),
                transactionHistory.getClassfication().getId());
    }
}
