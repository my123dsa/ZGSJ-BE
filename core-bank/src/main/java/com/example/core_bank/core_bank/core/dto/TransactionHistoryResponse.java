package com.example.core_bank.core_bank.core.dto;

import com.example.core_bank.core_bank.core.model.TransactionHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransactionHistoryResponse {

    private String transactionDate;
    private Integer amount;
    private Boolean isDeposit;
    private String transactionType;
    private Integer classificationCode;

    public static TransactionHistoryResponse from(TransactionHistory transaction) {
        return new TransactionHistoryResponse(
                transaction.getTransactionDate().toString(),
                transaction.getAmount(),
                transaction.getIsDeposit(),
                transaction.getTransactionType(),
                transaction.getClassification().getId()
        );
    }
}
