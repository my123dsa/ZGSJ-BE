package com.example.User.service;

import com.example.User.dto.authserver.AuthServerEmailPinNumberRequest;
import com.example.User.dto.authserver.AuthServerPinNumberRequest;
import com.example.User.dto.authserver.AuthServerProfileRequest;
import com.example.User.dto.corebank.AccountCheckRequest;
import com.example.User.dto.corebank.AccountInfoResponse;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.feign.CoreBankFeign;
import com.example.User.repository.PresidentRepository;
import com.example.User.repository.StoreRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoreBankService {

    private final StoreRepository storeRepository;
    private final PresidentRepository presidentRepository;
    private final CoreBankFeign coreBankFeign;

    public AccountInfoResponse getStoreAccountInfo(Integer storeId)
    {
        return AccountInfoResponse.from(storeRepository.findById(storeId).orElseThrow(
                () -> new CustomException(ErrorCode.NO_STORE)
        ));
    }

    //사장계좌 확인
    public boolean getNameByIdAndBankCodeAndAccountNumber(Integer id, String accountNumber, String bankCode) {
        // 1. id를 활용해 name을 조회한다.
        String name = presidentRepository.findNameById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRESIDENT_NOT_FOUND)); // custom exception 사용 가능

        log.info("이름 : " + name);
        // 2. 요청 DTO 생성
        AccountCheckRequest accountCheckRequest = AccountCheckRequest.of(accountNumber, bankCode, name);

        // 3. CoreBankFeign 호출
        try {
             return coreBankFeign.verifyAccount(accountCheckRequest);
        }catch (FeignException e){
            throw new CustomException(ErrorCode.BANK_ERROR);
        }
    }

    //직원계좌 확인
    public boolean getNameByIdAndBankCodeAndAccountNumberEmployeename(AccountCheckRequest accountCheckRequest) {
        // 1. CoreBankFeign 호출
        try {
            return coreBankFeign.verifyAccountEmployee(accountCheckRequest);
        }catch (FeignException e){
            throw new CustomException(ErrorCode.BANK_ERROR);
        }
    }

    public boolean verifyProfile(AuthServerProfileRequest profileRequest) {
        try {
            return  coreBankFeign.verifyProfile(profileRequest);
        }catch (FeignException e){
            throw new CustomException(ErrorCode.BANK_ERROR);
        }
    }

    public boolean checkEmailPinNumber(AuthServerEmailPinNumberRequest emailPinNumber) {
        try {
            return  coreBankFeign.checkEmailPinNumber(emailPinNumber);
        }catch (FeignException e){
            throw new CustomException(ErrorCode.BANK_ERROR);
        }
    }

    public boolean checkPinNumber(AuthServerPinNumberRequest checkPinNumber) {
        try {
            return  coreBankFeign.checkPinNumber(checkPinNumber);
        }catch (FeignException e){
            throw new CustomException(ErrorCode.BANK_ERROR);
        }
    }
}
