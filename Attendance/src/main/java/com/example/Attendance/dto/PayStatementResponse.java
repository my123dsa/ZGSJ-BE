package com.example.Attendance.dto;

import com.example.Attendance.model.PayStatement;
import com.example.Attendance.model.StoreEmployee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PayStatementResponse {
    private Integer payStatementId;
    private String name;
    private String code;
    private String accountNumber;
    private Integer amount;
    private LocalDate issuanceDate;

    public static PayStatementResponse of(PayStatement payStatement, StoreEmployee storeEmployee) {
        return new PayStatementResponse(
                payStatement.getId(),
                storeEmployee.getName(),
                storeEmployee.getBankCode(),
                storeEmployee.getAccountNumber(),
                payStatement.getAmount(),
                payStatement.getIssuanceDate()
        );
    }
}
