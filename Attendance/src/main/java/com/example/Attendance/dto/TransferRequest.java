package com.example.Attendance.dto;


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

    public static TransferRequest from(BatchInputData batchInputData) {
        return new TransferRequest(
                batchInputData.getFromAccount(),
                batchInputData.getFromBankCode(),
                batchInputData.getToAccount(),
                batchInputData.getToBankCode(),
                batchInputData.getAmount(),
                batchInputData.getToAccountDepositor(),
                batchInputData.getFromAccountDepositor()
        );
    }
}