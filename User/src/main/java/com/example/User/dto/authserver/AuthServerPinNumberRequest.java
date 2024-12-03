package com.example.User.dto.authserver;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class AuthServerPinNumberRequest {
    private String pinNumber;
    private String email;

    public static AuthServerPinNumberRequest of(String pinNumber, String email) {
        return new AuthServerPinNumberRequest(pinNumber, email);
    }
}
