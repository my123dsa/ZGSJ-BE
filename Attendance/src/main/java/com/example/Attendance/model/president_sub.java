package com.example.Attendance.model;

import jakarta.persistence.*;
import lombok.Getter;


import java.util.ArrayList;
import java.util.List;  // List import 추가

@Entity
@Table(name = "president_sub")
@Getter

public class president_sub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "president_id")
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "account_number", length = 50, nullable = false, unique = true)
    private String accountNumber;

    @OneToMany(mappedBy = "president")
    private List<employee_sub> employees = new ArrayList<>();
}