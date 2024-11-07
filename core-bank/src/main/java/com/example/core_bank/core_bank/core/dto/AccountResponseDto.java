package com.example.core_bank.core_bank.core.dto;

import com.example.core_bank.core_bank.core.model.Account;

public class AccountResponseDto {

    private String accountNumber;
    private String name;
    private Integer balance;
    private String bankName;
    private String bankCode;

    public AccountResponseDto(String accountNumber, String name, Integer balance, String bankName, String bankCode) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
        this.bankName = bankName;
        this.bankCode = bankCode;
    }

    public static AccountResponseDto from(Account account) {
        return new AccountResponseDto(
                account.getAccountNumber(),
                account.getName(),
                account.getBalance(),
                account.getBank().getBankName(),
                account.getBank().getBankCode()
        );
    }

    // Getters and Setters...
}
