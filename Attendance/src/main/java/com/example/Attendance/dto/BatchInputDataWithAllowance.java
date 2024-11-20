package com.example.Attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchInputDataWithAllowance {
    private Long total;
    private Long salary;
    private Long allowance;
    private Long charge;

    public static BatchInputDataWithAllowance of(
            Long allowance, Long total, Long salary,
            Long nationalCharge, Long insuranceCharge,Long employmentCharge) {

        Long charge = employmentCharge + insuranceCharge + nationalCharge;
        return new BatchInputDataWithAllowance(total, salary, allowance, charge);
    }
}