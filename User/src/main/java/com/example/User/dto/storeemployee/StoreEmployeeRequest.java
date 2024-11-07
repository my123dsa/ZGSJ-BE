package com.example.User.dto.storeemployee;

import com.example.User.model.Store;
import com.example.User.model.StoreEmployee;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreEmployeeRequest {

    private String email;
    private String name;
    private LocalDate birthDate;
    private Boolean sex;
    private String phoneNumber;
    private Boolean employmentType;
    private String bankCode;
    private String accountNumber;
    private Integer salary;
    @Min(1)
    @Max(28)
    private Integer paymentDate;
    private String address;

    private StoreEmployeeRequest(String email, String name, LocalDate birthDate,
                                 Boolean sex, String phoneNumber, Boolean employmentType,
                                 String bankCode, String accountNumber, Integer salary,
                                 Integer paymentDate, String address) {
        this.email = email;
        this.name = name;
        this.birthDate = birthDate;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.employmentType = employmentType;
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
        this.salary = salary;
        this.paymentDate = paymentDate;
        this.address = address;
    }

    public static StoreEmployeeRequest of(String email, String name, LocalDate birthDate,
                                          Boolean sex, String phoneNumber, Boolean employmentType,
                                          String bankCode, String accountNumber, Integer salary,
                                          Integer paymentDate, String address) {
        return new StoreEmployeeRequest(email, name, birthDate, sex, phoneNumber,
                employmentType, bankCode, accountNumber, salary,
                paymentDate, address);
    }

    public StoreEmployee toEntity(Store store) {
        return StoreEmployee.createStoreEmployee(name, sex, address, birthDate, phoneNumber,
                email, salary,employmentType, bankCode,accountNumber,paymentDate,store);
    }
}