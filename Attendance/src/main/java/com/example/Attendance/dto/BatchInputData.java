package com.example.Attendance.dto;

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
    private Boolean employmentType;
    private String toAccount;
    private String toBankCode;
    private Long salary;
    private String toAccountDepositor;
    private String fromAccountDepositor;

    public BatchInputData(Integer seId, String fromAccount,Boolean employmentType, String fromBankCode, String toAccount,
                          String toBankCode, Long salary, String toAccountDepositor, String fromAccountDepositor) {
        this.seId = seId;
        this.fromAccount = fromAccount;
        this.employmentType = employmentType;
        this.fromBankCode = fromBankCode;
        this.toAccount = toAccount;
        this.toBankCode = toBankCode;
        this.salary = salary;
        this.toAccountDepositor = toAccountDepositor;
        this.fromAccountDepositor = fromAccountDepositor;
    }

    public void changeSalary(Long commuteDuration){
        if (this.getEmploymentType()){ // true - 알바라고 가정
            // 분단위인데 시급 어케 줄지 기준이 30분 단위 들이다
            this.salary*=commuteDuration/60;
        }
    }
}