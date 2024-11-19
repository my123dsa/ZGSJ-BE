package com.example.User.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResIdFindData {
    private String email;

    public static ResIdFindData from(String email) {
        return new ResIdFindData(email);
    }
}
