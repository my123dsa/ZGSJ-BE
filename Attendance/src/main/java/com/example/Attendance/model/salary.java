package com.example.Attendance.model;

import jakarta.persistence.*;
import lombok.Getter;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "salary")
@Getter
public class salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_id")
    private Long id;

    @Column(name = "transfer_date", nullable = false)
    private LocalDate transferDate;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private employee_sub employee;

    @OneToMany(mappedBy = "salary")
    private List<pay_statement> payStatements = new ArrayList<>();
}