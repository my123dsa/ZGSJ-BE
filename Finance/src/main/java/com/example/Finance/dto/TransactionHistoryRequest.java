package com.example.Finance.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class TransactionHistoryRequest {
    private String account;
    private String bankCode;

    public static TransactionHistoryRequest from(AccountInfoResponse accountInfo) {
        TransactionHistoryRequest request = new TransactionHistoryRequest();
        request.setAccount(accountInfo.getAccountNumber());
        request.setBankCode(accountInfo.getBankCode());
        return request;
    }
}