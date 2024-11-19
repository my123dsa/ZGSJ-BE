package com.example.User.dto.corebank;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountCheckRequest {

    private final String name ;

    private final String bankCode ;

    @NotBlank // 유효성 검사를 추가
    private final String accountNumber;


    public static AccountCheckRequest of(String accountNumber, String bankCode, String name) {
        return new AccountCheckRequest(accountNumber, bankCode, name);
    }

}