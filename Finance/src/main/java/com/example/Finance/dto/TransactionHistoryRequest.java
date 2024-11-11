package com.example.Finance.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class TransactionHistoryRequest {

    private String depositor;
    private String account;
    private String bankCode;
}