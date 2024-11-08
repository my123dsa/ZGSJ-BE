package com.example.core_bank.core_bank.core.controller;

import com.example.core_bank.core_bank.core.dto.TransactionHistoryRequestDto;
import com.example.core_bank.core_bank.core.dto.TransactionHistoryResponse;
import com.example.core_bank.core_bank.core.model.Account;
import com.example.core_bank.core_bank.core.model.Classfication;
import com.example.core_bank.core_bank.core.model.TransactionHistory;
import com.example.core_bank.core_bank.core.service.TransactionHistoryService;
import com.example.core_bank.core_bank.core.repository.AccountRepository;
import com.example.core_bank.core_bank.core.repository.ClassficationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank")
@Slf4j
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;
    private final AccountRepository accountRepository;
    private final ClassficationRepository classficationRepository;

    @Autowired
    public TransactionHistoryController(TransactionHistoryService transactionHistoryService,
                                        AccountRepository accountRepository,
                                        ClassficationRepository classficationRepository) {
        this.transactionHistoryService = transactionHistoryService;
        this.accountRepository = accountRepository;
        this.classficationRepository = classficationRepository;
    }

    @PostMapping("/list")
    public ResponseEntity<List<TransactionHistoryResponse>> getTransactionHistory(@RequestBody TransactionHistoryRequestDto request) {

        // 요청에서 bankCode, accountNumber, depositor 정보를 받아옴
        String bankCode = request.getBankCode();
        String accountNumber = request.getAccount();
        String depositor = request.getDepositor();

        Account account = accountRepository.findByAccountNumber(accountNumber);
        log.info("account"+ account.getName());
//        List<Classfication> classfication = classficationRepository.findByClassficationName(depositor);  // 예시로 classficationName을 depositor로 사용

        // 거래 내역을 조회하고 반환
//        List<TransactionHistory> transactionHistoryList = transactionHistoryService.
//                getTransactionHistoryByAccountId(account.getId());
//        List<TransactionHistoryResponse> transactionHistoryres = transactionHistoryList
//                .stream()
//                .map(history-> TransactionHistoryResponse.of(history)).toList();

        List<TransactionHistoryResponse> transactionHistoryres = transactionHistoryService
                .getTransactionHistoryByAccountId(account.getId())
                .stream()
                .map(TransactionHistoryResponse::of)
                .toList();

        return ResponseEntity.ok(transactionHistoryres);
    }
}
