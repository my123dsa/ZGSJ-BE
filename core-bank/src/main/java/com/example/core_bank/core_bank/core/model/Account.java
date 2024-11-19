package com.example.core_bank.core_bank.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private Integer id;

    @Column(name = "account_number", nullable = false, unique = true, length = 50)
    private String accountNumber;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "balance", nullable = false)
    private Long balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    private Account(String accountNumber, String name, Long balance, Bank bank) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
        this.bank = bank;
    }

    public static Account createAccountWithBalance(String accountNumber, String name, Bank bank, Long balance) {
        return new Account(accountNumber, name, balance, bank);
    }

    public static Account createAccount(String accountNumber, String name, Bank bank) {
        return new Account(accountNumber, name, 0L, bank);  // 초기 잔액 0
    }
}
