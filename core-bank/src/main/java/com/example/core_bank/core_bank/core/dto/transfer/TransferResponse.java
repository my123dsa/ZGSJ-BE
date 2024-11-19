package com.example.core_bank.core_bank.core.dto.transfer;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TransferResponse {
    private Integer status;
    private LocalDate issuanceDate;
    private String message;
    public static TransferResponse of(Integer status, LocalDate issuanceDate, String message) {

        return new TransferResponse(status, issuanceDate, message);
    }
}
