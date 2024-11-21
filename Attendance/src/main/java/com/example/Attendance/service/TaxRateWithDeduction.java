package com.example.Attendance.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaxRateWithDeduction {
    private final double rate;
    private final long deduction;

    public static TaxRateWithDeduction of(double rate, long deduction) {
        return new TaxRateWithDeduction(rate, deduction);
    }
}
