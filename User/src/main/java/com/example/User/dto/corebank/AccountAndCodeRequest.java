package com.example.User.dto.corebank;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountAndCodeRequest {

    @NotBlank
    private String bankCode ;

    @NotBlank // 유효성 검사를 추가
    private String accountNumber;

    public static AccountAndCodeRequest of(String accountNumber, String bankCode) {
        return new AccountAndCodeRequest(bankCode,accountNumber);
    }

}