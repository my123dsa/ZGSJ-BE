package com.example.Finance.service;

import com.example.Finance.dto.TransactionHistoryRequest;
import com.example.Finance.dto.TransactionHistoryResponse;
import com.example.Finance.feign.CoreBankFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionHistoryService {
    private final CoreBankFeign coreBankFeign;

    public List<TransactionHistoryResponse> getTransactionHistoryList(TransactionHistoryRequest transactionHistoryRequest)
    {
        return coreBankFeign.getTransactionHistoryList(transactionHistoryRequest);
    }


}
