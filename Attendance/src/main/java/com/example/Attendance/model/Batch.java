package com.example.Attendance.model;

import com.example.Attendance.dto.batch.BatchOutputData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "batch_table")
@Getter
@NoArgsConstructor
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "se_id", nullable = false)
    private Integer seId;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "issuance_date", nullable = false)
    private LocalDate issuanceDate;

    @Column(name = "message")
    private String message;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "salary",nullable = false)
    private Long salary;

    @Column(name = "allowance",nullable = false)
    private Long allowance;

    @Column(name = "national_charge",nullable = false)
    private Long nationalCharge;

    @Column(name = "insurance_charge",nullable = false)
    private Long insuranceCharge;

    @Column(name = "employment_charge",nullable = false)
    private Long employmentCharge;

    @Column(name = "income_tax",nullable = false)
    private Long incomeTax;

    @Column(name = "president_email", length = 100,nullable = false)
    private String presidentEmail;

    @Column(name = "employee_email", length = 100,nullable = false)
    private String employeeEmail;

    @Column(name = "pdf_url")
    private String url;

    @Column(name = "bank_result",nullable = false)
    private Boolean bankResult;

    @Column(name = "pdf_result",nullable = false)
    private Boolean pdfResult;

    @Column(name = "email_result",nullable = false)
    private Boolean emailResult;

    @Column(name = "is_mask",nullable = false)
    private Boolean isMask;

    @Column(name = "is_charge",nullable = false)
    private Boolean isCharge;


    public static Batch from(BatchOutputData outputData) {
        return new Batch(
                outputData.getSeId(),            // 1
                outputData.getStatus(),          // 2
                outputData.getIssuanceDate(),    // 3
                outputData.getMessage(),         // 4
                outputData.getName(),            // 5
                outputData.getBirthDate(),       // 6
                outputData.getPhoneNumber(),     // 7
                outputData.getSalary(),          // 8
                outputData.getAllowance(),       // 9
                outputData.getNationalCharge(),  // 10
                outputData.getInsuranceCharge(), // 11
                outputData.getEmploymentCharge(),// 12
                outputData.getIncomeTax(),       // 13
                outputData.getPresidentEmail(),  // 14
                outputData.getEmployeeEmail(),   // 15
                null,                           // url
                outputData.getBankResult(),     // bankResult
                false,                          // pdfResult
                false,                           // emailResult
                outputData.getIsMask(),
                outputData.getIsCharge()
        );
    }

    public Batch(Integer seId,                  // 1
                 Integer status,                 // 2
                 LocalDate issuanceDate,         // 3
                 String message,                 // 4
                 String name,                    // 5
                 LocalDate birthDate,            // 6
                 String phoneNumber,             // 7
                 Long salary,                    // 8
                 Long allowance,                 // 9
                 Long nationalCharge,            // 10
                 Long insuranceCharge,           // 11
                 Long employmentCharge,          // 12
                 Long incomeTax,                 // 13
                 String presidentEmail,          // 14
                 String employeeEmail,           // 15
                 String url,                     // 16
                 Boolean bankResult,             // 17
                 Boolean pdfResult,              // 18
                 Boolean emailResult,
                 Boolean isMask,
                 Boolean isCharge) {          // 19
        this.seId = seId;
        this.status = status;
        this.issuanceDate = issuanceDate;
        this.message = message;
        this.name = name;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.salary = salary;
        this.allowance = allowance;
        this.nationalCharge = nationalCharge;
        this.insuranceCharge = insuranceCharge;
        this.employmentCharge = employmentCharge;
        this.incomeTax = incomeTax;
        this.presidentEmail = presidentEmail;
        this.employeeEmail = employeeEmail;
        this.url = url;
        this.bankResult = bankResult;
        this.pdfResult = pdfResult;
        this.emailResult = emailResult;
        this.isMask= isMask;
        this.isCharge= isCharge;
    }
}
