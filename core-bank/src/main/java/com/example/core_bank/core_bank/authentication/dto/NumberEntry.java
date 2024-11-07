package com.example.core_bank.core_bank.authentication.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
}
