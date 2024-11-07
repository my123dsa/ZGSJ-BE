package com.example.core_bank.core_bank.core.dto;

public class TransactionHistoryRequestDto {

    private String depositor;
    private String account;
    private String bankCode;

    // Getters and Setters

    public String getDepositor() {
        return depositor;
    }

    public void setDepositor(String depositor) {
        this.depositor = depositor;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}