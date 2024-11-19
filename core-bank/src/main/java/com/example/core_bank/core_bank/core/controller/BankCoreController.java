package com.example.core_bank.core_bank.core.controller;

import com.example.core_bank.core_bank.core.dto.transfer.TransferRequest;
import com.example.core_bank.core_bank.core.dto.transfer.TransferResponse;
import com.example.core_bank.core_bank.core.service.BankCoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bank")
public class BankCoreController {

    private final BankCoreService bankCoreService;

    @PostMapping("/automatictransfer")
    public TransferResponse automaticTransfer(@RequestBody TransferRequest transferRequest) {
        log.info("automatictransfer");

        LocalDate transferDate= bankCoreService.transfer(transferRequest);
        return TransferResponse.of(200,transferDate,"success");
    }
}