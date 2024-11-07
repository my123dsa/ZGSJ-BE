package com.example.core_bank.core_bank.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bank")
@Getter
@NoArgsConstructor
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_id")
    private Integer id;

    @Column(name= "bank_code", nullable = false, length = 50)
    private String bankCode ;

    @Column(name= "bank_name", nullable = false, length = 50)
    private String bankName ;

    private Bank(String bankCode, String bankName) {
        this.bankCode = bankCode;
        this.bankName = bankName;
    }

    public static Bank createBank(String bankCode, String bankName) {
        return new Bank(bankCode, bankName);
    }

}
