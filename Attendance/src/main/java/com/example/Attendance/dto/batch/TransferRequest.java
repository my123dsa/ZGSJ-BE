package com.example.Attendance.dto.batch;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}