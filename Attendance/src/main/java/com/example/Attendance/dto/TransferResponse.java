package com.example.Attendance.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransferResponse {
    private Integer status;
    private LocalDateTime issuanceDate ;
    private String message;

    public TransferResponse(Integer status, LocalDateTime issuanceDate, String message) {
        this.status = status;
        this.issuanceDate = issuanceDate;
        this.message = message;
    }

    public static TransferResponse of(Integer status,LocalDateTime issuanceDate, String message) {

        return new TransferResponse(status,issuanceDate,message);
    }
}