package com.example.User.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Boolean sex;

    @Column(nullable = false, length = 150)
    private String address;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "employment_type", nullable = false)
    private Boolean employmentType;

    @Column(name = "phone_number", nullable = false, length = 50, unique = true)
    private String phoneNumber;

    @Column(name = "payment_date", nullable = false)
    private Integer paymentDate;

    @Column(nullable = false)
    private Integer salary;

    @Column(name = "account_number", nullable = false, length = 50, unique = true)
    private String accountNumber;

    @Column(name = "bank_code", nullable = false, length = 50)
    private String bankCode;

    @Column(nullable = false, length = 50)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "president_id", nullable = false)
    private President president;
}
