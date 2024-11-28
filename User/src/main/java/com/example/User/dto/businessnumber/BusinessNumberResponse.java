package com.example.User.dto.businessnumber;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BusinessNumberResponse {
    private boolean exists;
    private boolean hasError;
    private String message;

    public static BusinessNumberResponse of(boolean exists, boolean hasError, String message) {
        return new BusinessNumberResponse(exists, hasError, message);
    }
}