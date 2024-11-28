package com.example.User.controller;

import com.example.User.dto.corebank.AccountAndCodeRequest;
import com.example.User.dto.corebank.AccountCheckRequest;
import com.example.User.dto.corebank.AccountInfoResponse;
import com.example.User.resolver.MasterId;
import com.example.User.service.CoreBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CoreBankController {

    private final CoreBankService coreBankService;

    @GetMapping("/storeAccount")
    public AccountInfoResponse getaccountInfoResponse(@RequestParam Integer storeId)
    {
        return coreBankService.getStoreAccountInfo(storeId);
    }

    //사장계좌확인
    @PostMapping("/account-check")
    public ResponseEntity<Boolean> getAccountBankcodeAndAccountNumber(
            @MasterId Integer id,
            @RequestBody AccountAndCodeRequest accountAndCodeRequest) {
        // 서비스 호출
        boolean isValid = coreBankService.getNameByIdAndBankCodeAndAccountNumber(
                id,
                accountAndCodeRequest.getAccountNumber(),
                accountAndCodeRequest.getBankCode()
        );

        // 프론트로 boolean 값 반환
        return ResponseEntity.ok(isValid);
    }

    //직원계좌확인
    @PostMapping("/employee-account-check")
    public ResponseEntity<Boolean> getAccountBankcodeAndAccountNumberEmployeename(
            @RequestBody AccountCheckRequest accountCheckRequest) {
        // 서비스 호출
        boolean isValid = coreBankService.getNameByIdAndBankCodeAndAccountNumberEmployeename(accountCheckRequest);

        // 프론트로 boolean 값 반환
        return ResponseEntity.ok(isValid);
    }




}
