package com.example.User.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "store_employee",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"store_id", "email"}
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

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Boolean sex;

    @Column(nullable = false, length = 150)
    private String address;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "phone_number", nullable = false, length = 50, unique = true)
    private String phoneNumber;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false)
    private Integer salary;

    @Column(name = "employment_type", nullable = false)
    private Boolean employmentType;

    @Column(name = "bank_code", nullable = false, length = 50)
    private String bankCode;

    @Column(name = "account_number", nullable = false, length = 50)
    private String accountNumber;

    @Column(name = "payment_date", nullable = false)
    @Min(1)
    @Max(28)
    private Integer paymentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private StoreEmployee(String name, Boolean sex, String address, LocalDate birthDate, String phoneNumber,
                          String email, Integer salary, Boolean employmentType, String bankCode,
                          String accountNumber, Integer paymentDate, Store store) {
        this.name = name;
        this.sex = sex;
        this.address = address;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.salary = salary;
        this.employmentType = employmentType;
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
        this.paymentDate = paymentDate;
        this.store = store;
    }

    public static StoreEmployee createStoreEmployee(String name, Boolean sex, String address, LocalDate birthDate,
                                                    String phoneNumber, String email, Integer salary,
                                                    Boolean employmentType, String bankCode, String accountNumber,
                                                    Integer paymentDate, Store store) {
        return new StoreEmployee(name, sex, address, birthDate, phoneNumber, email, salary,
                employmentType, bankCode, accountNumber, paymentDate, store);
    }
}