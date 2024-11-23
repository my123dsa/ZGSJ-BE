package com.example.User.dto.authserver;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AuthServerProfileRequest {
    private String name;
    private String email;
}
