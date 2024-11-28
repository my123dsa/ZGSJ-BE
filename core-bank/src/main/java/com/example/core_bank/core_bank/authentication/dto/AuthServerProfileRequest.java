package com.example.core_bank.core_bank.authentication.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AuthServerProfileRequest {
    private String name;
    private String email;
}
