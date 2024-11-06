package com.example.core_bank.core_bank.controller;

import com.example.core_bank.core_bank.dto.businessnumber.BusinessNumberResponse;
import com.example.core_bank.core_bank.service.BusinessNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BusinessNumberController {

    private final BusinessNumberService businessNumberService;

    @PostMapping("/businesscheck")
    public BusinessNumberResponse checkBusinessNumber(@RequestBody String businessNumber) {
        return businessNumberService.existsByBusinessNumber(businessNumber);
    }
}
