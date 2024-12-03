package com.example.User.dto.authserver;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AuthServerOnlyPinNumberRequest {
    private String pinNumber;
}
