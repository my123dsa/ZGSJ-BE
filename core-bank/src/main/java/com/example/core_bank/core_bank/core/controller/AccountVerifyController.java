package com.example.core_bank.core_bank.core.controller;

import com.example.core_bank.core_bank.core.dto.accountnumber.AccountVerifyRequest;
import com.example.core_bank.core_bank.core.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountVerifyController {

    private final AccountService accountService;

    @PostMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestBody AccountVerifyRequest request) {
//        boolean exists = accountService.isAccountExists(request.getName(), request.getAccountNumber(), request.getBankCode());
//        if (exists) {
//            return ResponseEntity.ok("계좌가 존재합니다.");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("계좌가 존재하지 않습니다.");
//        }

        boolean exists = accountService.isAccountExists(
                request.getName(),
                request.getAccountNumber(),
                request.getBankCode()
        );
        return exists ? ResponseEntity.ok("계좌가 존재합니다.")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("계좌가 존재하지 않습니다.");
    }
}
