package com.example.Attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BatchOutputData {
    private Integer seId;
    private Integer status;
    private LocalDateTime issuanceDate;
    private String message;
    private Long amount;

    public BatchOutputData(Integer seId, Integer status, LocalDateTime issuanceDate, String message,Long amount) {
        this.seId = seId;
        this.status = status;
        this.issuanceDate = issuanceDate;
        this.message = message;
        this.amount = amount;
    }

    public static BatchOutputData of(Integer seId, Long amount,TransferResponse transferResponse) {

        return new BatchOutputData(seId,transferResponse.getStatus(),LocalDateTime.now(),transferResponse.getMessage(),amount);
    }
}
