package com.example.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransactionHistoryResponse {

    private String transactionDate;
    private Integer amount;
    private Boolean isDeposit;
    private String transactionType;
    private String classificationName;

}
