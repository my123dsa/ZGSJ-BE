package com.example.User.service;

import com.example.User.dto.corebank.AccountInfoResponse;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoreBankService {

    private final StoreRepository storeRepository;

    public AccountInfoResponse getStoreAccountInfo(Integer storeId)
    {
        return AccountInfoResponse.from(storeRepository.findById(storeId).orElseThrow(
                () -> new CustomException(ErrorCode.NO_STORE)
        ));
    }
}
