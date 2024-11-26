package com.example.Attendance.service.batch;


import com.example.Attendance.dto.batch.BatchInputData;
import com.example.Attendance.dto.batch.CommuteSummary;
import com.example.Attendance.dto.batch.TaxRateWithDeduction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalculateService {
    private final AttendanceConstants AC;

    // 완전 일일알바일경우 3.3%
    public void calculate(BatchInputData bid, CommuteSummary commuteSummary) {
        Long allowance = allowance(bid.getEmploymentType(), commuteSummary, bid.getSalary());
        Long salary = salary(bid.getEmploymentType(), bid.getSalary(), commuteSummary.getCommuteDuration());
        Long total = allowance + salary;

        Long nationalCharge = nationalCharge(total, bid.getEmploymentType());
        Long insuranceCharge = insuranceCharge(total, bid.getEmploymentType());
        Long employmentCharge = employmentCharge(total, bid.getEmploymentType());
        Long incomeTax = incomeTax(total, bid.getEmploymentType());
        log.info("계산 값: total: {} salary: {} nation: {} insurance: {} employmentCharge: {} incomeTax: {}",
                total, salary, nationalCharge, insuranceCharge, employmentCharge, incomeTax);

        bid.setSalaryAfter(salary);
        bid.setAllowance(allowance);

        bid.setEmploymentCharge(employmentCharge);
        bid.setInsuranceCharge(insuranceCharge);
        bid.setNationalCharge(nationalCharge);
        bid.setIncomeTax(incomeTax);
    }

    public long incomeTax(Long total, Byte type) {
        if (type == 4) {  //일일 근로자?
            return 0L;
        } else if (type == 2) { //알바 3.3
            return Math.round(total * 0.033);
        }
        TaxRateWithDeduction bracket = AC.whatBracket(total);
        return Math.round(bracket.getRate() * total) - bracket.getDeduction();
    }

    public long salary(Byte type, Long salary, Long commuteDuration) {
        if (type == 1) //이후 6도 추가
            return salary;
        return salary * (commuteDuration);
    }

    public long nationalCharge(Long total, Byte type) {
        if (type != 1) { //3번은 4대보험 됨
            return 0L;
        }
        if (total > AC.NATIONAL_PENSION_LIMIT_UPPER) {
            return BigDecimal.valueOf(AC.NATIONAL_PENSION_LIMIT_UPPER * 0.045)
                    .setScale(-3, RoundingMode.HALF_UP)
                    .longValue();
        }
        return BigDecimal.valueOf(total * 0.045)
                .setScale(-3, RoundingMode.HALF_UP)
                .longValue();
    }

    public long insuranceCharge(Long total, Byte type) {
        if (type != 1) { //3번은 4대보험 됨
            return 0L;
        }
        if (total > AC.HEALTH_INSURANCE_LIMIT_UPPER) {
            return Math.round(AC.HEALTH_INSURANCE_LIMIT_UPPER * 0.040041);
        }
        return Math.round(total * 0.040041);
    }

    public long employmentCharge(Long total, Byte type) {
        if (type != 1) {
            return 0L;
        }
        return Math.round(total * 0.009);
    }

    public long allowance(Byte type, CommuteSummary commuteSummary, Long salary) {
        if (type == 1 || type == 4) // 포괄임금 및 일일 근무자
            return 0L;

        if (type == 6) //수당 받는 직원
            return salary / 5;

        int weekTime = (int) Math.floor(commuteSummary.getCommuteDuration() /
                (double) (commuteSummary.getDayLength() / 7));
        if (weekTime >= 15)
            return weekTime * salary / 5;
        return 0L;
    }
}
