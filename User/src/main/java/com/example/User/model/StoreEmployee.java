package com.example.User.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store_employee")
@Getter
@NoArgsConstructor
public class StoreEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "se_id")
    private Integer id;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;

    @Column(nullable = false)
    private Integer salary; // 시급 or 월급

    @Column(name = "employment_type", nullable = false)
    private Boolean employmentType; // 직원 유형 (직원 or 알바)

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

    private StoreEmployee(Integer storeId, Integer employeeId, Integer salary,
                         Boolean employmentType, String bankCode, String accountNumber,
                         Integer paymentDate, Store store, Employee employee) {
        this.storeId = storeId;
        this.employeeId = employeeId;
        this.salary = salary;
        this.employmentType = employmentType;
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
        this.paymentDate = paymentDate;
        this.store = store;
        this.employee = employee;
    }

    public static StoreEmployee createStoreEmployee(Integer storeId, Integer employeeId, Integer salary,
                                                    Boolean employmentType, String bankCode, String accountNumber,
                                                    Integer paymentDate, Store store, Employee employee) {
        return new StoreEmployee(storeId, employeeId, salary, employmentType, bankCode, accountNumber, paymentDate, store, employee);
    }

}
