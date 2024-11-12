package com.example.Finance.controller;

import com.example.Finance.dto.AccountInfoResponse;
import com.example.Finance.dto.TransactionHistoryRequest;
import com.example.Finance.dto.TransactionHistoryResponse;
import com.example.Finance.feign.UserFeign;
import com.example.Finance.service.IncomeStatementService;
import com.example.Finance.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FinanceController {

    private final TransactionHistoryService transactionHistoryService;
    private final IncomeStatementService incomeStatementService;
    private final UserFeign userFeign;

    @GetMapping("/dotest")
    public List<TransactionHistoryResponse> getFinanaceData(
            @RequestParam Integer storeid,
            @RequestParam Integer year,
            @RequestParam Integer month)
    {
        TransactionHistoryRequest transactionHistoryRequest= TransactionHistoryRequest.from(userFeign.getStoreAccountInfo(storeid));
        log.info(transactionHistoryRequest.toString());
        return transactionHistoryService.getTransactionHistoryList(transactionHistoryRequest, year ,month);
    }



}
