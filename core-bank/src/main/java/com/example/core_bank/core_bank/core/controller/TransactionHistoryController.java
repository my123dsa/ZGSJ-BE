package com.example.core_bank.core_bank.core.controller;

import com.example.core_bank.core_bank.core.dto.TransactionHistoryRequestDto;
import com.example.core_bank.core_bank.core.dto.TransactionHistoryResponse;
import com.example.core_bank.core_bank.core.model.Account;
import com.example.core_bank.core_bank.core.service.TransactionHistoryService;
import com.example.core_bank.core_bank.core.repository.AccountRepository;
import com.example.core_bank.core_bank.core.repository.ClassficationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank")
@Slf4j
@RequiredArgsConstructor
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    @PostMapping("/list")
    public ResponseEntity<List<TransactionHistoryResponse>> getTransactionHistory(
            @RequestBody TransactionHistoryRequestDto request,
            @RequestParam Integer year,
            @RequestParam Integer month)
    {

        // 요청에서 bankCode, accountNumber, depositor 정보를 받아옴
        String bankCode = request.getBankCode();
        String accountNumber = request.getAccount();
        String depositor = request.getDepositor();

        // 서비스 레이어를 통해 거래 내역을 조회
        List<TransactionHistoryResponse> transactionHistoryRes = transactionHistoryService
                .getTransactionHistory(bankCode, accountNumber, year, month);

        return ResponseEntity.ok(transactionHistoryRes);
    }
}
