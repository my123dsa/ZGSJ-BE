package com.example.User.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResNewAccessToken {
    private String accessToken;

    public static ResNewAccessToken from(String accessToken) {
        return new ResNewAccessToken(accessToken);
    }
}
