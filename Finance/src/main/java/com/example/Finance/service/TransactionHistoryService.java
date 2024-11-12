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
    private final IncomeStatementService incomeStatementService;

    public List<TransactionHistoryResponse> getTransactionHistoryList(
            TransactionHistoryRequest transactionHistoryRequest,
            Integer year,
            Integer month)
    {
        incomeStatementService.calculateStatement(coreBankFeign.getTransactionHistoryList(transactionHistoryRequest ,year, month));
        return coreBankFeign.getTransactionHistoryList(transactionHistoryRequest ,year, month);
    }


}
