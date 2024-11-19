package com.example.User.controller;

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

    @PostMapping("/account-check")
    public ResponseEntity<Boolean> getAccountBankcodeAndAccountNumber(
            @MasterId Integer id,
            @RequestBody AccountCheckRequest accountCheckRequest
    ) {
        // 서비스 호출
        boolean isValid = coreBankService.getNameByIdAndBankCodeAndAccountNumber(
                id,
                accountCheckRequest.getAccountNumber(),
                accountCheckRequest.getBankCode()
        );

        // 프론트로 boolean 값 반환
        return ResponseEntity.ok(isValid);
    }
}
