package com.example.User.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "president")
public class President {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "president_id", nullable = false)
    private Integer presidentId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 150)
    private String address;

    @Column(name = "business_number", nullable = false, length = 50, unique = true)
    private String businessNumber;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "account_number", nullable = false, length = 50, unique = true)
    private String accountNumber;

    @Column(name = "phone_number", nullable = false, length = 50, unique = true)
    private String phoneNumber;

    @Column(name = "temrs_accept", nullable = false)
    private Boolean temrsAccept; // tinyInt를 boolean으로 설정

    @OneToMany(mappedBy = "president")
    private List<Employee> employees;
}
