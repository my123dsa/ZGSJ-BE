package com.example.Attendance.service;


import com.example.Attendance.dto.BatchInputData;
import com.example.Attendance.dto.BatchInputDataWithAllowance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@RequiredArgsConstructor
@Service
public class BigService {
    private final AttendanceConstants AC;

    // 완전 일일알바일경우 3.3%
    public BatchInputDataWithAllowance calculate(BatchInputData bid, Long commuteDuration) {
        Long allowance = allowance(bid.getEmploymentType(), commuteDuration, bid.getSalary());
        Long salary = salary(bid.getEmploymentType(), bid.getSalary(), commuteDuration);
        Long total = allowance + salary;

        Long nationalCharge = nationalCharge(total);
        Long insuranceCharge = insuranceCharge(total);
        Long employmentCharge = employmentCharge(total);
        Long incomeTax=incomeTax(total,bid.getEmploymentType());
        return BatchInputDataWithAllowance.of
                (allowance, total, salary, nationalCharge, insuranceCharge, employmentCharge,incomeTax);
    }

    public long incomeTax(Long total,Byte type){

        if (type==1){
            return 0L;
        } else if (type==2){
            return Math.round(total*0.033);
        }
        TaxRateWithDeduction bracket =AC.whatBracket(total);
        return Math.round(bracket.getRate()*total)-bracket.getDeduction();
    }

    public long salary(Byte type, Long salary, Long commuteDuration) {
        if (type==1)
            return salary;
        return salary * (commuteDuration / 60);
    }

    public long nationalCharge(Long total) {
        if (total <= AC.NATIONAL_PENSION_LIMIT_LOWER) {
            return BigDecimal.valueOf(AC.NATIONAL_PENSION_LIMIT_LOWER * 0.045)
                    .setScale(-3, RoundingMode.HALF_UP)
                    .longValue();
        } else if (total > AC.NATIONAL_PENSION_LIMIT_UPPER) {
            return BigDecimal.valueOf(AC.NATIONAL_PENSION_LIMIT_UPPER * 0.045)
                    .setScale(-3, RoundingMode.HALF_UP)
                    .longValue();
        }
        return BigDecimal.valueOf(total * 0.045)
                .setScale(-3, RoundingMode.HALF_UP)
                .longValue();
    }

    public long insuranceCharge(Long total) {
        if (total <= AC.HEALTH_INSURANCE_LIMIT_LOWER) {
            return Math.round(AC.HEALTH_INSURANCE_LIMIT_LOWER * 0.040041);
        } else if (total > AC.HEALTH_INSURANCE_LIMIT_UPPER) {
            return Math.round(AC.HEALTH_INSURANCE_LIMIT_UPPER * 0.040041);
        }
        return Math.round(total * 0.040041);
    }

    public long employmentCharge(Long total) {
        return Math.round(total * 0.009);
    }

    public long allowance(Byte type, Long commuteDuration, Long salary) {
        if (type != 4 && commuteDuration >= 60) {
            return (long) Math.ceil(4.345 * 4.345 * salary * commuteDuration);
        }
        return 0L;
    }
}
