package com.example.User.dto.authserver;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AuthServerPinNumberRequest {
    private String pinNumber;
    private String email;
}
