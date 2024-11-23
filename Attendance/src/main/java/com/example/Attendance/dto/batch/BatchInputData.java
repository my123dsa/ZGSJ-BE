package com.example.Attendance.dto.batch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchInputData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer seId;
    private String fromAccount;
    private String fromBankCode;
    private Byte employmentType;
    private String toAccount;
    private String toBankCode;
    private Long salary;
    private String toAccountDepositor;
    private String fromAccountDepositor;
    private String email;
    private LocalDate birthDate;
    private String phoneNumber;

    private Long salaryAfter;
    private Long allowance;
    private Long nationalCharge;
    private Long insuranceCharge;
    private Long employmentCharge;
    private Long incomeTax;

    private int retryCount = 0;

    public BatchInputData(Integer seId, String fromAccount, String fromBankCode, Byte employmentType,
                          String toAccount, String toBankCode, Long salary, String toAccountDepositor,
                          String fromAccountDepositor, String email, LocalDate birthDate, String phoneNumber) {
        this.seId = seId;
        this.fromAccount = fromAccount;
        this.fromBankCode = fromBankCode;
        this.employmentType = employmentType;
        this.toAccount = toAccount;
        this.toBankCode = toBankCode;
        this.salary = salary;
        this.toAccountDepositor = toAccountDepositor;
        this.fromAccountDepositor = fromAccountDepositor;
        this.email = email;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }
}