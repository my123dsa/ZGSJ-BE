package com.example.User.service;

import com.example.User.dto.businessnumber.BusinessNumberRequest;
import com.example.User.dto.businessnumber.BusinessNumberResponse;
import com.example.User.feign.CoreBankFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final CoreBankFeign coreBankFeign;

    public BusinessNumberResponse validateBusinessNumber(BusinessNumberRequest businessNumberRequest) {
        return coreBankFeign.checkBusinessNumber(businessNumberRequest.getBusinessNumber());
    }
}
