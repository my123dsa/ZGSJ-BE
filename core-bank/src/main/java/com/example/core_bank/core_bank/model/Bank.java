package com.example.core_bank.core_bank.model;

import jakarta.persistence.*;
import lombok.Builder;
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
    private int id;

    @Column(name= "bank_code", nullable = false, length = 50)
    private String bankCode ;

    @Column(name= "bank_name", nullable = false, length = 50)
    private String bankName ;

    @Builder
    public Bank(String bankCode, String bankName) {
        this.bankCode = bankCode;
        this.bankName = bankName;
    }

}
