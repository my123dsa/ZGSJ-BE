package com.example.core_bank.core_bank.dto.businessnumber;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BusinessNumberResponse {
    private boolean exists;
    private boolean hasError;
    private String message;

    public static BusinessNumberResponse of(boolean exists, boolean hasError, String message) {
        return new BusinessNumberResponse(exists,hasError, message);
    }
}