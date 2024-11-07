package com.example.core_bank.core_bank.authentication.controller;

import com.example.core_bank.core_bank.authentication.dto.ReqAuthentication;
import com.example.core_bank.core_bank.authentication.service.AuthenticationService;
import com.example.core_bank.core_bank.authentication.service.EmailService;
import com.example.core_bank.core_bank.authentication.service.VerificationNumberStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/verify/pinNumber")
    public ResponseEntity<?> checkEmailPinNumber(@RequestBody ReqAuthentication reqAuthentication) {
        log.info("Check email pin number" + reqAuthentication.getEmail());
        boolean result = authenticationService.checkEmailAndPinNumber(reqAuthentication);
        if (!result)
            return ResponseEntity.badRequest().build();

        String verificationNumber = emailService.sendVerificationEmail(reqAuthentication.getEmail());
        verificationNumberStorage.saveNumber(reqAuthentication.getEmail(), verificationNumber);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify/emailCode")
    public ResponseEntity<?> checkEmailNumber(@RequestBody ReqAuthentication reqAuthentication) {
        log.info("Check email pin number: " + reqAuthentication.getEmail());
        boolean result = verificationNumberStorage.verifyNumber(reqAuthentication);

        if (!result)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok().build();
    }
}
