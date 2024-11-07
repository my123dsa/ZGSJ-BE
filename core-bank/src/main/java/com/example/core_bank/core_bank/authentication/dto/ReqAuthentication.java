package com.example.core_bank.core_bank.authentication.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ReqAuthentication {
    private String email;
    private String pinNumber;
}
