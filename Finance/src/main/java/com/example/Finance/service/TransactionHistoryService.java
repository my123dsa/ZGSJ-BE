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

    //요청받은 REQUEST에 따라, LIST로 거래 내역 받아오기
    public List<TransactionHistoryResponse> getTransactionHistoryList(
            TransactionHistoryRequest transactionHistoryRequest,
            Integer year,
            Integer month)
    {
        return coreBankFeign.getTransactionHistoryList(transactionHistoryRequest, year, month);
    }


}
