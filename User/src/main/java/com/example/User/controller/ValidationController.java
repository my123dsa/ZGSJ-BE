package com.example.User.controller;

import com.example.User.dto.businessnumber.BusinessNumberRequest;
import com.example.User.dto.businessnumber.BusinessNumberResponse;
import com.example.User.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ValidationController {

    private final ValidationService validationService;

    @PostMapping("/store/businesscheck")
    public ResponseEntity<Boolean> validateBusinessNumber(@RequestBody BusinessNumberRequest businessNumberRequest)
    {
        BusinessNumberResponse response = validationService.validateBusinessNumber(businessNumberRequest);
        return ResponseEntity.ok(response.isExists());
    }
}
