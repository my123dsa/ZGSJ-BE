package com.example.Finance.controller;

import com.example.Finance.dto.TransactionHistoryRequest;
import com.example.Finance.dto.TransactionHistoryResponse;
import com.example.Finance.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FinanceController {

    private final TransactionHistoryService transactionHistoryService;

    @GetMapping("/dotest")
    public List<TransactionHistoryResponse> getFinanaceData(
            @RequestBody TransactionHistoryRequest transactionHistoryRequest,
            @RequestParam Integer year,
            @RequestParam Integer month)
    {
        return transactionHistoryService.getTransactionHistoryList(transactionHistoryRequest, year ,month);
    }

}
