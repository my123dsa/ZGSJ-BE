package com.example.core_bank.core_bank.core.service;

import com.example.core_bank.core_bank.core.dto.businessnumber.BusinessNumberResponse;
import com.example.core_bank.core_bank.global.error.CustomException;
import com.example.core_bank.core_bank.global.error.ErrorCode;
import com.example.core_bank.core_bank.core.repository.BusinessNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessNumberService {
    private final BusinessNumberRepository businessNumberRepository;

    public BusinessNumberResponse existsByBusinessNumber(String businessNumber) {
        try {
            boolean exists = businessNumberRepository.existsByBusinessNumber(businessNumber);
            return BusinessNumberResponse.of(exists,false,exists ?
                    "등록된 사업자번호입니다." : "미등록된 사업자번호입니다.");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

}
