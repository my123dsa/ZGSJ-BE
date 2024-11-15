package com.example.Finance.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransactionHistoryRequest {
    private String account;
    private String bankCode;

    public static TransactionHistoryRequest from(AccountInfoResponse accountInfo) {
        return new TransactionHistoryRequest(accountInfo.accountNumber, accountInfo.getBankCode());
    }
}