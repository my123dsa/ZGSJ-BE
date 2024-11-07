package com.example.core_bank.core_bank.authentication.service;

import com.example.core_bank.core_bank.authentication.dto.ReqAuthentication;
import com.example.core_bank.core_bank.authentication.model.Authentication;
import com.example.core_bank.core_bank.authentication.repository.AuthenticationRepository;
import com.example.core_bank.core_bank.global.error.CustomException;
import com.example.core_bank.core_bank.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationRepository authenticationRepository;

    public boolean checkEmailAndPinNumber(ReqAuthentication reqAuthentication) {
        Authentication authentication = authenticationRepository
                .findByEmail(reqAuthentication.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        log.info("email: " + authentication.getEmail() + " " + authentication.getPinNumber());
        return authentication.getPinNumber().equals(reqAuthentication.getPinNumber());
    }
}
