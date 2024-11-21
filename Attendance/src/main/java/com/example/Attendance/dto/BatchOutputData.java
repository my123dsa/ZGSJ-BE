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
    private String email;
    private LocalDate birthDate;
    private String phoneNumber;


    private Long total;
    private Long salary;
    private Long allowance;
    private Long charge;

    public static BatchOutputData of(BatchInputDataWithAllowance bidwa, TransferResponse transferResponse,BatchInputData bid ) {

        return new BatchOutputData(bid.getSeId(), transferResponse.getStatus(),
                transferResponse.getIssuanceDate(), transferResponse.getMessage()
                ,bid.getEmail(),bid.getBirthDate(),bid.getPhoneNumber()
                , bidwa.getTotal(),bidwa.getSalary(),bidwa.getAllowance(),bidwa.getCharge());
    }

    public PayStatement toEntity() {
        return PayStatement.createPayStatement("12342412", this.getIssuanceDate(),this.getSeId(), (int)(this.getTotal()-this.getCharge()) );
    }
}
