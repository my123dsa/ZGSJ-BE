package com.example.Attendance.dto;

import com.example.Attendance.model.PayStatement;
import com.example.Attendance.model.StoreEmployee;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private LocalDateTime issuanceDate;
    private String message;

    private Long total;
    private Long salary;
    private Long allowance;
    private Long charge;

    public static BatchOutputData of(BatchInputDataWithAllowance bidwa, TransferResponse transferResponse,Integer seId) {

        return new BatchOutputData(seId, transferResponse.getStatus(),
                transferResponse.getIssuanceDate(), transferResponse.getMessage(), bidwa.getTotal()
                ,bidwa.getSalary(),bidwa.getAllowance(),bidwa.getCharge());
    }

    public PayStatement toEntity() {
        return PayStatement.createPayStatement("12342412", this.getIssuanceDate().toLocalDate(),this.getSeId(), this.getTotal().intValue());
    }
}
