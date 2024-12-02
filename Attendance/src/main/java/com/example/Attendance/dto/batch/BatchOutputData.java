package com.example.Attendance.dto.batch;

import com.example.Attendance.error.ErrorDTO;
import com.example.Attendance.model.Batch;
import com.example.Attendance.model.PayStatement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@Slf4j
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

    private String presidentEmail;
    private String employeeEmail;
    private Boolean bankResult;

    private Boolean isMask;

    private Boolean isCharge;

    public static BatchOutputData of(TransferResponse transferResponse, BatchInputData bid,boolean isCharge) {
        boolean result= transferResponse.getStatus() ==200;
        boolean mask= bid.getEmploymentType() ==10;
        log.info("여기요 {}",result);
        return new BatchOutputData(bid.getSeId(), transferResponse.getStatus(),
                transferResponse.getIssuanceDate(), transferResponse.getMessage()
                , bid.getToAccountDepositor(), bid.getEmail(), bid.getBirthDate(), bid.getPhoneNumber()
                , bid.getSalaryAfter(), bid.getAllowance()
                , bid.getNationalCharge(), bid.getInsuranceCharge(), bid.getEmploymentCharge(), bid.getIncomeTax()
        ,bid.getPresidentEmail(),bid.getEmail(),result,mask, isCharge);
    }

    public static BatchOutputData ofFail(BatchInputData bid, ErrorDTO dto) {
        boolean mask= bid.getEmploymentType() ==10;
        return new BatchOutputData(bid.getSeId(), 400,
                LocalDate.now(), dto.getCode()
                , bid.getToAccountDepositor(), bid.getEmail(), bid.getBirthDate(), bid.getPhoneNumber()
                , bid.getSalaryAfter(), bid.getAllowance()
                , bid.getNationalCharge(), bid.getInsuranceCharge(), bid.getEmploymentCharge(), bid.getIncomeTax()
                ,bid.getPresidentEmail(),bid.getEmail(),false,mask,false);
    }

    public Batch ToBatchEntity() {
        return Batch.from(this);
    }
}
