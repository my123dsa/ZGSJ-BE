package com.example.User.dto.storeemployee;

import com.example.User.model.StoreEmployee;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmployeeAutoTransferResponse {
    private String name;
    private String accountNumber;
    private Integer salary;
    private Integer paymentDate;

    private EmployeeAutoTransferResponse(String name, String accountNumber,
                                         Integer salary, Integer paymentDate) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.salary = salary;
        this.paymentDate = paymentDate;
    }

    public static EmployeeAutoTransferResponse from(StoreEmployee employee) {
        return new EmployeeAutoTransferResponse(
                employee.getName(),
                employee.getAccountNumber(),
                employee.getSalary(),
                employee.getPaymentDate()
        );
    }
}