package com.example.Attendance.dto.batch.email;

import com.example.Attendance.config.attendanceJob.Batch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailInputData {

    private Integer batchId;
    private String name;
    private String employeeEmail;
    private String presidentEmail;
    private String url;
    private Boolean bankResult;
    private Boolean pdfResult;
    private LocalDate issuanceDate;
    private String message;

    public static EmailInputData from(Batch batch) {
        return new EmailInputData(
                batch.getId(),
                batch.getName(),
                batch.getEmployeeEmail(),
                batch.getPresidentEmail(),
                batch.getUrl(),
                batch.getBankResult(),
                batch.getPdfResult(),
                batch.getIssuanceDate(),
                batch.getMessage()
        );
    }
}
