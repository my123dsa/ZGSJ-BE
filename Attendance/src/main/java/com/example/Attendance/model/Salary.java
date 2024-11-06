package com.example.Attendance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "salary")
@Getter
@NoArgsConstructor
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_id")
    private Integer id;

    @Column(name = "transfer_date", nullable = false)
    private LocalDate transferDate;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EmployeeSub employeeSub;

    @OneToMany(mappedBy = "salary")
    private List<PayStatement> payStatements = new ArrayList<>();

    private Salary(LocalDate transferDate, Integer amount, EmployeeSub employeeSub) {
        this.transferDate = transferDate;
        this.amount = amount;
        this.employeeSub = employeeSub;
    }

    public static Salary createSalary(EmployeeSub employeeSub) {
        return new Salary(LocalDate.now(), 0, employeeSub);
    }
}