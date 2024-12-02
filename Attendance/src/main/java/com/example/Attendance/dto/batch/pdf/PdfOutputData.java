package com.example.Attendance.dto.batch.pdf;

import com.example.Attendance.model.PayStatement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PdfOutputData {
    private Integer batchId;
    private Integer seId;
    private String url;
    private LocalDate issuanceDate;
    private Integer amount;
    private Boolean pdfResult;


    public static PdfOutputData of(PdfInputData data,String url,Boolean pdfResult) {
        long amount= data.getSalary()+ data.getAllowance()
                - data.getNationalCharge()- data.getInsuranceCharge()
                - data.getEmploymentCharge()- data.getIncomeTax();
        return new PdfOutputData(data.getBatchId(),data.getSeId(),url,data.getIssuanceDate(),(int)amount,pdfResult);
    }
    public PayStatement toEntity(){
        return PayStatement.createPayStatement(url,issuanceDate,seId,amount);
    }
    public PdfSaveData toPdfSaveData(){
        return PdfSaveData.of(this.batchId,this.url);
    }
}
