package com.example.core_bank.core_bank.core.dto;


import lombok.*;

@NoArgsConstructor
@Setter
@Getter
public class TransactionHistoryRequestDto {

    private String depositor;
    private String account;
    private String bankCode;
}