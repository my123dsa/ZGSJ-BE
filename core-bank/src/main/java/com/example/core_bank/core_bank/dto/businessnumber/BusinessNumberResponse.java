package com.example.core_bank.core_bank.dto.businessnumber;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessNumberResponse {
    private boolean exists;
    private boolean hasError;
    private String message;
}