package com.example.User.service;

import com.example.User.dto.businessnumber.BusinessNumberRequest;
import com.example.User.dto.businessnumber.BusinessNumberResponse;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.feign.CoreBankFeign;
import com.example.User.repository.PresidentRepository;
import com.example.User.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final CoreBankFeign coreBankFeign;
    private final StoreRepository storeRepository;

    public BusinessNumberResponse validateBusinessNumber(BusinessNumberRequest businessNumberRequest) {
         boolean result=  storeRepository.existsByStoreName(businessNumberRequest.getStoreName());
         if (result)
             throw new CustomException(ErrorCode.DUPLICATE_STORE_NAME);
        return coreBankFeign.checkBusinessNumber(businessNumberRequest.getBusinessNumber());
    }
}
