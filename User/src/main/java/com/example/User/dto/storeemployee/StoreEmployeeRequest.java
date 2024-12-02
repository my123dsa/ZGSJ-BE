package com.example.User.dto.storeemployee;

import com.example.User.model.Store;
import com.example.User.model.StoreEmployee;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor (access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class StoreEmployeeRequest {

    @Email
    private String email;
    private String name;
    private LocalDate birthDate;
    private Boolean sex;
    private String phoneNumber;
    private Byte employmentType;
    private String bankCode;
    private String accountNumber;
    private Integer salary;
    @Min(1)
    @Max(28)
    private Integer paymentDate;
    private String address;

    public static StoreEmployeeRequest of(String email, String name, LocalDate birthDate,
                                          Boolean sex, String phoneNumber, Byte employmentType,
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