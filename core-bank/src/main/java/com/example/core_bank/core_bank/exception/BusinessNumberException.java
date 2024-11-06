package com.example.core_bank.core_bank.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class BusinessNumberException extends RuntimeException {
    String message;
}
