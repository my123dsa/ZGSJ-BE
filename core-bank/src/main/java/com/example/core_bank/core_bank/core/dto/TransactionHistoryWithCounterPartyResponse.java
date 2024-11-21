package com.example.core_bank.core_bank.core.dto;

import com.example.core_bank.core_bank.core.model.TransactionHistory;
import lombok.Getter;

@Getter
public class TransactionHistoryWithCounterPartyResponse extends TransactionHistoryResponse {
    private String counterpartyName;

    public TransactionHistoryWithCounterPartyResponse(String transactionDate, Long amount, Boolean isDeposit,
                                                      String transactionType, String classificationName, String counterpartyName) {
        super(transactionDate, amount, isDeposit, transactionType, classificationName);  // 부모 클래스 생성자 호출
        this.counterpartyName = counterpartyName;
    }

    public static TransactionHistoryWithCounterPartyResponse from(TransactionHistory transaction) {
        return new TransactionHistoryWithCounterPartyResponse(
                transaction.getTransactionDate().toString(),
                transaction.getAmount(),
                transaction.getIsDeposit(),
                transaction.getTransactionType(),
                transaction.getClassfication().getClassficationName(),
                transaction.getCounterpartyName()
        );
    }
}
