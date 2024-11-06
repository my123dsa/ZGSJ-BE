package com.example.Attendance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employee_sub")
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeSub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "employment_type", nullable = false)
    private Boolean employmentType;

    @Column(name = "phone_number", length = 30, nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "payment_date", nullable = false)
    private Integer paymentDate;

    @Column(name = "salary", nullable = false)
    private Integer salary;

    @Column(name = "account_number", length = 50, nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "bank_code", length = 50, nullable = false)
    private String bankCode;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "president_id")
    private PresidentSub presidentSub;

    @OneToMany(mappedBy = "employeeSub")
    private List<Working> workings = new ArrayList<>();

    @OneToMany(mappedBy = "employeeSub")
    private List<Salary> salaries = new ArrayList<>();

}