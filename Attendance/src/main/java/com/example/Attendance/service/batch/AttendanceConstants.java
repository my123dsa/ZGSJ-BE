package com.example.Attendance.service.batch;

import com.example.Attendance.dto.batch.TaxRateWithDeduction;
import org.springframework.stereotype.Component;


@Component
public class AttendanceConstants {
    // National Pension Limits
    public final long NATIONAL_PENSION_LIMIT_UPPER = 6_170_000L;

    // Health Insurance Limits
    public final long HEALTH_INSURANCE_LIMIT_UPPER = 119_625_106L;

    // Income Tax Rates
    public final double TAX_RATE_1 = 0.06;
    public final double TAX_RATE_2 = 0.15;
    public final double TAX_RATE_3 = 0.24;
    public final double TAX_RATE_4 = 0.35;
    public final double TAX_RATE_5 = 0.38;
    public final double TAX_RATE_6 = 0.40;
    public final double TAX_RATE_7 = 0.42;
    public final double TAX_RATE_8 = 0.45;

    // Tax Brackets
    public final long TAX_BRACKET_1 = 1_166_666L;
    public final long TAX_BRACKET_2 = 4_166_666L;
    public final long TAX_BRACKET_3 = 7_333_333L;
    public final long TAX_BRACKET_4 = 12_500_000L;
    public final long TAX_BRACKET_5 = 25_000_000L;
    public final long TAX_BRACKET_6 = 41_666_666L;
    public final long TAX_BRACKET_7 = 83_333_333L;

    // Tax Deductions
    public final long DEDUCTION_1 = 0L;
    public final long DEDUCTION_2 = 105_000L;
    public final long DEDUCTION_3 = 480_000L;
    public final long DEDUCTION_4 = 1_286_666L;
    public final long DEDUCTION_5 = 1_661_666L;
    public final long DEDUCTION_6 = 2_161_666L;
    public final long DEDUCTION_7 = 2_995_000L;
    public final long DEDUCTION_8 = 5_495_000L;

    public final TaxRateWithDeduction[] TAX_INFO = {
            TaxRateWithDeduction.of(TAX_RATE_1, DEDUCTION_1),
            TaxRateWithDeduction.of(TAX_RATE_2, DEDUCTION_2),
            TaxRateWithDeduction.of(TAX_RATE_3, DEDUCTION_3),
            TaxRateWithDeduction.of(TAX_RATE_4, DEDUCTION_4),
            TaxRateWithDeduction.of(TAX_RATE_5, DEDUCTION_5),
            TaxRateWithDeduction.of(TAX_RATE_6, DEDUCTION_6),
            TaxRateWithDeduction.of(TAX_RATE_7, DEDUCTION_7),
            TaxRateWithDeduction.of(TAX_RATE_8, DEDUCTION_8)
    };

    public TaxRateWithDeduction whatBracket(Long total) {
        if (total <= TAX_BRACKET_1)
            return TAX_INFO[0];
        if (total <= TAX_BRACKET_2)
            return TAX_INFO[1];
        if (total <= TAX_BRACKET_3)
            return TAX_INFO[2];
        if (total <= TAX_BRACKET_4)
            return TAX_INFO[3];
        if (total <= TAX_BRACKET_5)
            return TAX_INFO[4];
        if (total <= TAX_BRACKET_6)
            return TAX_INFO[5];
        if (total <= TAX_BRACKET_7)
            return TAX_INFO[6];
        return TAX_INFO[7];
    }
}
