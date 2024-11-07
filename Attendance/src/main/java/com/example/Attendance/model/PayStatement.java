package com.example.Attendance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "pay_statement")
@Getter
@NoArgsConstructor
public class PayStatement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "url", length = 150, nullable = false)
    private String url;

    @Column(name = "issuance_date", nullable = false)
    private LocalDate issuanceDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "se_id")
    private StoreEmployee storeEmployee;

    private PayStatement(String url, LocalDate issuanceDate, StoreEmployee storeEmployee) {
        this.url = url;
        this.issuanceDate = issuanceDate;
        this.storeEmployee = storeEmployee;
    }

    public static PayStatement createPayStatement(String url, LocalDate issuanceDate, StoreEmployee storeEmployee) {
        return new PayStatement(url, issuanceDate, storeEmployee);
    }
}