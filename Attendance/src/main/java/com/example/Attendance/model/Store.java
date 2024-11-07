package com.example.Attendance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store")
@Getter
@NoArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "store_name", length = 50, nullable = false, unique = true)
    private String storeName;

    @Column(name = "account_number", length = 50, nullable = false)
    private String accountNumber;

    @Column(name = "bank_code", length = 50, nullable = false)
    private String bankCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "president_id")
    private President president;
}
