package com.example.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryWithCounterPartyResponse extends TransactionHistoryRequest {
    private String counterpartyName;

}
