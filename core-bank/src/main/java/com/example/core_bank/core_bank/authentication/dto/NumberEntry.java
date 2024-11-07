package com.example.core_bank.core_bank.authentication.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NumberEntry {
    private String number;
    private LocalDateTime expiryTime;

    public boolean isValid() {
        return isValid(LocalDateTime.now());
    }

    public boolean isValid(LocalDateTime now) {
        return now.isBefore(expiryTime);
    }

    public static NumberEntry of(String number, LocalDateTime expiryTime) {
        return new NumberEntry(number, expiryTime);
    }
}
