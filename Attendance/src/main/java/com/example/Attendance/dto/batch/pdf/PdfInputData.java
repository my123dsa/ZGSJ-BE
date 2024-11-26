package com.example.Attendance.dto.batch.pdf;

import com.example.Attendance.model.Batch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PdfInputData {

    private Integer batchId;
    private Integer seId;
    private Integer status;
    private LocalDate issuanceDate;
    private String message;
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

    public static PdfInputData from(Batch batch) {
        return new PdfInputData(
                batch.getId(),
                batch.getSeId(),
                batch.getStatus(),
                batch.getIssuanceDate(),
                batch.getMessage(),
                batch.getName(),
                batch.getEmployeeEmail(),
                batch.getBirthDate(),
                batch.getPhoneNumber(),
                batch.getSalary(),
                batch.getAllowance(),
                batch.getNationalCharge(),
                batch.getInsuranceCharge(),
                batch.getEmploymentCharge(),
                batch.getIncomeTax()
        );
    }
}
