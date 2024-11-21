package com.example.core_bank.core_bank.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "transaction_history")
@Getter
@NoArgsConstructor
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "th_id")
    private Integer id;

    @Column(name= "transaction_date", nullable = false)
    private LocalDate transactionDate ;

    @Column(name= "amount", nullable = false)
    private Long amount;

    @Column(columnDefinition = "TINYINT(1)", name = "is_deposit", nullable = false)
    private Boolean isDeposit;

    @Column(name = "transaction_type", nullable = false, length = 50)
    private String transactionType;

    @Column(name = "counterparty_name", length = 100)
    private String counterpartyName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classfication_id")
    private Classfication classfication;

    public TransactionHistory(LocalDate transactionDate, Long amount, boolean isDeposit, String transactionType, String counterpartyName, Account account, Classfication classfication) {
        this.transactionDate = transactionDate;
        this.amount= amount;
        this.isDeposit = isDeposit;
        this.transactionType = transactionType;
        this.counterpartyName = counterpartyName;
        this.account = account;
        this.classfication = classfication;
    }

    public static TransactionHistory createTransactionHistory(LocalDate transactionDate, Long amount, boolean isDeposit, String transactionType, String counterpartyName, Account account, Classfication classfication)
    {
        return new TransactionHistory(transactionDate, amount, isDeposit, transactionType, counterpartyName, account, classfication);
    }

    // classfication 객체 반환 메소드
    public Classfication getClassification() {
        return this.classfication;
    }
}
