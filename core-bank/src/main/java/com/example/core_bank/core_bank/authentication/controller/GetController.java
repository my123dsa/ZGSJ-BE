package com.example.core_bank.core_bank.authentication.controller;

import com.example.core_bank.core_bank.authentication.dto.TransferRequest;
import com.example.core_bank.core_bank.authentication.dto.TransferResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bank")
public class GetController {
    @PostMapping("/automaticTransfer")
    public TransferResponse automaticTransfer(@RequestBody TransferRequest transferRequest) {
        log.info("automaticTransfer");
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setMessage("success");
        transferResponse.setIssuanceDate(LocalDateTime.now());
        transferResponse.setStatus(200);
        return transferResponse;
    }
}
