package com.example.Attendance.dto.batch;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    private String fromAccount;
    private String fromBankCode;
    private String toAccount;
    private String toBankCode;
    private Long amount;
    private String toAccountDepositor;
    private String fromAccountDepositor;

    public static TransferRequest from(BatchInputData bid) {
        Long amount = bid.getSalaryAfter() + bid.getAllowance()
                - bid.getNationalCharge() - bid.getEmploymentCharge()
                - bid.getInsuranceCharge() - bid.getIncomeTax();
        return new TransferRequest(
                bid.getFromAccount(),
                bid.getFromBankCode(),
                bid.getToAccount(),
                bid.getToBankCode(),
                amount,
                bid.getToAccountDepositor(),
                bid.getFromAccountDepositor()
        );
    }

    public static TransferRequest fromForAdmin(TransferRequest request) {
        BigDecimal amount = new BigDecimal(request.getAmount())
                .multiply(new BigDecimal("0.001"))
                .setScale(0, RoundingMode.DOWN);  // 소수점 이하 버림
        return new TransferRequest(
                request.getFromAccount(),
                request.getFromBankCode(),
                "98765432112",
                "020",
                amount.longValue(),
                "집계 사장",
                request.getFromAccountDepositor()
        );
    }
}