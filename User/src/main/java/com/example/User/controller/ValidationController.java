package com.example.User.controller;

import com.example.User.dto.businessnumber.BusinessNumberRequest;
import com.example.User.dto.businessnumber.BusinessNumberResponse;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
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

    @PostMapping("/businesscheck")
    public ResponseEntity<String> validateBusinessNumber(@RequestBody BusinessNumberRequest businessNumberRequest)
    {
        BusinessNumberResponse response = validationService.validateBusinessNumber(businessNumberRequest);

        //이 에러 처리 부분 위에 통합하는게 좋을 듯?
        if (!response.isExists())
            throw new CustomException(ErrorCode.BUSINESSNUMBER_NOT_FOUND);
        return ResponseEntity.ok().build();
    }
}
