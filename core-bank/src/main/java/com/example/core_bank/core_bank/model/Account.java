package com.example.core_bank.core_bank.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @Builder
    public Account(String accountNumber, String name, int balance, @NonNull Bank bank) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
        this.bank = bank;
    }
}
