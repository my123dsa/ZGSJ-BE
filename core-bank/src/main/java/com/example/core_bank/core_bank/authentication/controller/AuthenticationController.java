package com.example.core_bank.core_bank.authentication.controller;

import com.example.core_bank.core_bank.authentication.dto.AuthServerEmailPinNumberRequest;
import com.example.core_bank.core_bank.authentication.dto.AuthServerPinNumberRequest;
import com.example.core_bank.core_bank.authentication.dto.AuthServerProfileRequest;
import com.example.core_bank.core_bank.authentication.service.AuthenticationService;
import com.example.core_bank.core_bank.authentication.service.EmailService;
import com.example.core_bank.core_bank.authentication.service.VerificationNumberStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final VerificationNumberStorage verificationNumberStorage;
    private final EmailService emailService;

    @PostMapping("/pincheck")
    public boolean verifyPinNumberAndEmail(@RequestBody AuthServerPinNumberRequest pinNumberRequest) {
        log.info("Check email pin number" + pinNumberRequest.getEmail());
        boolean result = authenticationService.checkEmailAndPinNumber(pinNumberRequest);
        if (!result)
            return false;

        return true;
    }

    @PostMapping("/email/pincheck")
    public boolean verifyEmailCode(@RequestBody AuthServerEmailPinNumberRequest emailPinNumberRequest) {
        boolean result = verificationNumberStorage.verifyNumber(emailPinNumberRequest);

        if (!result)
            return false;
        return true;
    }

    @PostMapping("/profile/check")
    public boolean verifyProfile(@RequestBody AuthServerProfileRequest profileRequest) {
        boolean result = authenticationService.verifyProfile(profileRequest);
        if (!result)
            return false;
        String verificationNumber = emailService.sendVerificationEmail(profileRequest.getEmail());
        verificationNumberStorage.saveNumber(profileRequest.getEmail(), verificationNumber);
        return true;
    }
}
