package com.example.User.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "president")
public class President {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "president_id")
    private Integer presidentId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "address", nullable = false, length = 150)
    private String address;

    @Column(name = "business_number", nullable = false, length = 50, unique = true)
    private String businessNumber;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "account_number", nullable = false, length = 50, unique = true)
    private String accountNumber;

    @Column(name = "phone_number", nullable = false, length = 50)
    private String phoneNumber;

    @Column(name = "temrs_accept", nullable = false)
    private Boolean temrsAccept;

    @OneToMany(mappedBy = "president")
    private List<Employee> employees;
}
