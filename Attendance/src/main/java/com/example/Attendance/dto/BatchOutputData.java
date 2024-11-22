package com.example.Attendance.dto;

import com.example.Attendance.model.PayStatement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchOutputData {
    private Integer seId;
    private Integer status;
    private LocalDate issuanceDate;
    private String message;
    // 일 한 기록
    //이름 //전화번호, 생년월일 필요함
    private String name;
    private String email;
    private LocalDate birthDate;
    private String phoneNumber;

    private Long salary;
    private Long allowance;
    private Long nationalCharge;
    private Long insuranceCharge;
    private Long employmentCharge;
    private Long incomeTax;

    public static BatchOutputData of(TransferResponse transferResponse,BatchInputData bid ) {

        return new BatchOutputData(bid.getSeId(), transferResponse.getStatus(),
                transferResponse.getIssuanceDate(), transferResponse.getMessage()
                ,bid.getToAccountDepositor() ,bid.getEmail(),bid.getBirthDate(),bid.getPhoneNumber()
                , bid.getSalaryAfter(),bid.getAllowance()
                ,bid.getNationalCharge(),bid.getInsuranceCharge(),bid.getEmploymentCharge(),bid.getIncomeTax());
    }

    public PayStatement toEntity(String url) {
        long amount =salary+allowance
                - nationalCharge- employmentCharge
                - insuranceCharge -incomeTax;
        return PayStatement.createPayStatement(url, this.getIssuanceDate(),this.getSeId(), (int)amount);
    }
}
