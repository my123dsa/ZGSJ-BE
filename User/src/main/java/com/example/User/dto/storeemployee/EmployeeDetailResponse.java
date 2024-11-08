package com.example.User.dto.storeemployee;

import com.example.User.model.StoreEmployee;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmployeeDetailResponse {
    private String name;
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;

    public EmployeeDetailResponse(String name, LocalDate birthDate, String phoneNumber, String address) {
        this.name = name;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public static EmployeeDetailResponse from(StoreEmployee employee) {
        return new EmployeeDetailResponse(
                employee.getName(),
                employee.getBirthDate(),
                employee.getPhoneNumber(),
                employee.getAddress()
        );
    }

}
