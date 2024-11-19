package com.example.User.dto.corebank;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountCheckRequest {

    private String name ;

    private String bankCode ;

    private String accountNumber;


    public static AccountCheckRequest of(String accountNumber, String bankCode, String name) {
        return new AccountCheckRequest(name,bankCode,accountNumber);
    }
}