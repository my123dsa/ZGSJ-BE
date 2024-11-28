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

    public String validateBusinessNumber(BusinessNumberRequest businessNumberRequest) {
        if (storeRepository.existsByStoreName(businessNumberRequest.getStoreName())) {
            return "이미 존재하는 가게 명입니다";
        }
        return coreBankFeign.checkBusinessNumber(businessNumberRequest.getBusinessNumber()).isExists()
                ? "ok"
                : "존재하지 않는 사업자번호입니다";
    }
}
