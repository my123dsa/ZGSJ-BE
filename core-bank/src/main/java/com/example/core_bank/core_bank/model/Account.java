package com.example.core_bank.core_bank.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account")
@Getter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private int id;

    @Column(name = "account_number", nullable = false, unique = true, length = 50)
    private String accountNumber;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "balance", nullable = false)
    private int balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    private Account(String accountNumber, String name, int balance, Bank bank) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
        this.bank = bank;
    }

    public static Account createAccountWithBalance(String accountNumber, String name, Bank bank, int balance) {
        return new Account(accountNumber, name, balance, bank);
    }

    public static Account createAccount(String accountNumber, String name, Bank bank) {
        return new Account(accountNumber, name, 0, bank);  // 초기 잔액 0
    }
}
