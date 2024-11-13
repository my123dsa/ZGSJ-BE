package com.example.Attendance.dto;

import com.example.Attendance.model.StoreEmployee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BatchInputData {
    private Integer seId;
    private String fromAccount;
    private String fromBankCode;
    private String toAccount;
    private String toBankCode;
    private Long amount;
    private String toAccountDepositor;
    private String fromAccountDepositor;

    public BatchInputData(Integer seId, String fromAccount, String fromBankCode, String toAccount,
                          String toBankCode, Long amount, String toAccountDepositor, String fromAccountDepositor) {
        this.seId = seId;
        this.fromAccount = fromAccount;
        this.fromBankCode = fromBankCode;
        this.toAccount = toAccount;
        this.toBankCode = toBankCode;
        this.amount = amount;
        this.toAccountDepositor = toAccountDepositor;
        this.fromAccountDepositor = fromAccountDepositor;
    }

    public static BatchInputData of(StoreEmployee se,Long commuteDuration) {

        Long amount= se.getSalary();
        if (se.getEmploymentType()){
            //분단위인데 시급 어케 줄지 기준이 30분 단위 들이다
            amount*=commuteDuration/60;
        }

        return new BatchInputData(se.getId(),se.getStore().getAccountNumber(),"020",se.getAccountNumber()
                ,se.getBankCode(),amount,se.getName(),se.getStore().getPresident().getName());
    }
}