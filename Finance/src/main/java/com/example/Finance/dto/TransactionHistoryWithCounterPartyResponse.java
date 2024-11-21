package com.example.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class TransactionHistoryWithCounterPartyResponse extends TransactionHistoryResponse {
    private String counterpartyName;

    public TransactionHistoryWithCounterPartyResponse(String transactionDate, Integer amount, Boolean isDeposit, String transactionType, String classificationName) {
        super(transactionDate, amount, isDeposit, transactionType, classificationName);
    }
}
