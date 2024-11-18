package com.example.Attendance.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "pay_statement")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayStatement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ps_id")
    private Integer id;

    @Column(name = "url", length = 150, nullable = false)
    private String url;

    @Column(name = "issuance_date", nullable = false)
    private LocalDate issuanceDate;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @JoinColumn(name = "se_id")
    private Integer seId;

    private PayStatement(String url, LocalDate issuanceDate, Integer seId, Integer amount) {
        this.url = url;
        this.issuanceDate = issuanceDate;
        this.seId = seId;
        this.amount = amount;
    }

    public static PayStatement createPayStatement(String url, LocalDate issuanceDate, Integer seId, Integer amount) {
        return new PayStatement(url, issuanceDate, seId,amount);
    }
}