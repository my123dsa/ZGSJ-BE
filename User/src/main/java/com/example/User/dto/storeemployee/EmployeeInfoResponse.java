package com.example.User.dto.storeemployee;

import com.example.User.model.StoreEmployee;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmployeeInfoResponse {
    private Integer id;
    private String name;
    private Boolean sex;
    private String address;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private Integer salary;
    private Byte employmentType;
    private String bankCode;
    private String accountNumber;
    private Integer paymentDate;

    public static EmployeeInfoResponse from(StoreEmployee employee) {
        return new EmployeeInfoResponse(
                employee.getId(),
                employee.getName(),
                employee.getSex(),
                employee.getAddress(),
                employee.getBirthDate(),
                employee.getPhoneNumber(),
                employee.getEmail(),
                employee.getSalary(),
                employee.getEmploymentType(),
                employee.getBankCode(),
                employee.getAccountNumber(),
                employee.getPaymentDate()
        );
    }
}
