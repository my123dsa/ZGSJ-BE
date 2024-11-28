package com.example.core_bank.core_bank.authentication.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AuthServerEmailPinNumberRequest {
    private String emailPinNumber;
    private String email;
}
