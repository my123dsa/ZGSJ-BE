package com.example.core_bank.core_bank.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Entity
@Table(name = "transaction_history")
@Getter
@NoArgsConstructor
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "th_id")
    private int id;

    @Column(name= "transaction_date", nullable = false)
    private LocalDate transactionDate ;

    @Column(name= "amount", nullable = false)
    private int amount;

    @Column(columnDefinition = "TINYINT(1)", name = "is_deposit", nullable = false)
    private boolean isDeposit;

    @Column(name = "transaction_type", nullable = false, length = 50)
    private String transactionType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "classfication_id")
    private Classfication classfication;

    @Builder
    public TransactionHistory(LocalDate transactionDate, boolean isDeposit, String transactionType, @NonNull Account account, @NonNull Classfication classfication) {
        this.transactionDate = transactionDate;
        this.isDeposit = isDeposit;
        this.transactionType = transactionType;
        this.account = account;
        this.classfication = classfication;
    }

}
