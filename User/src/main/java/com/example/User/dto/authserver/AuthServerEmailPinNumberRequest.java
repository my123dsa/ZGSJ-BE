package com.example.User.dto.authserver;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AuthServerEmailPinNumberRequest {
    private String emailPinNumber;
    private String email;
}
