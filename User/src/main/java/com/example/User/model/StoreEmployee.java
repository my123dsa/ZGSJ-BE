package com.example.User.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store_employee",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"store_id", "employee_id"}
                )
        }
)
@Getter
@NoArgsConstructor
public class StoreEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "se_id")
    private Integer id;

    @Column(nullable = false)
    private Integer salary;

    @Column(name = "employment_type", nullable = false)
    private Boolean employmentType;

    @Column(name = "bank_code", nullable = false, length = 50)
    private String bankCode;

    @Column(name = "account_number", nullable = false, length = 50)
    private String accountNumber;

    @Column(nullable = false)
    @Min(1)
    @Max(28)
    private Integer paymentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private StoreEmployee(Integer salary, Boolean employmentType, String bankCode,
                          String accountNumber, Integer paymentDate, Store store, Employee employee) {
        this.salary = salary;
        this.employmentType = employmentType;
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
        this.paymentDate = paymentDate;
        this.store = store;
        this.employee = employee;
    }

    public static StoreEmployee createStoreEmployee(Integer salary, Boolean employmentType, String bankCode,
                                                    String accountNumber, Integer paymentDate, Store store, Employee employee) {
        return new StoreEmployee(salary, employmentType, bankCode, accountNumber, paymentDate, store, employee);
    }
}