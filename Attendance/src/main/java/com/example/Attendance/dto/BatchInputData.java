package com.example.Attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchInputData {
    private Integer seId;
    private String fromAccount;
    private String fromBankCode;
    private Byte employmentType;
    private String toAccount;
    private String toBankCode;
    private Long salary;
    private String toAccountDepositor;
    private String fromAccountDepositor;
}