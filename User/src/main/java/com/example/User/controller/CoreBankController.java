package com.example.User.controller;

import com.example.User.dto.corebank.AccountInfoResponse;
import com.example.User.service.CoreBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CoreBankController {

    private final CoreBankService coreBankService;

    @GetMapping("/storeAccount")
    public AccountInfoResponse getaccountInfoResponse(@RequestParam Integer storeId)
    {
        return coreBankService.getStoreAccountInfo(storeId);
    }
}
