package com.example.User.controller;

import com.example.User.dto.authserver.AuthServerEmailPinNumberRequest;
import com.example.User.dto.authserver.AuthServerOnlyPinNumberRequest;
import com.example.User.dto.authserver.AuthServerPinNumberRequest;
import com.example.User.dto.corebank.AccountAndCodeRequest;
import com.example.User.dto.authserver.AuthServerProfileRequest;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.resolver.MasterId;
import com.example.User.service.CoreBankService;
import com.example.User.service.PresidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/core/account")
public class AccountAuthController {

    private final CoreBankService coreBankService;
    private final PresidentService presidentService;

    @PostMapping("/check")
    public ResponseEntity<Boolean> getAccountBankCodeAndAccountNumber(
            @MasterId Integer id,
            @RequestBody AccountAndCodeRequest accountAndCodeRequest) {
        // 서비스 호출
        boolean isValid = coreBankService.getNameByIdAndBankCodeAndAccountNumber(
                id,
                accountAndCodeRequest.getAccountNumber(),
                accountAndCodeRequest.getBankCode()
        );

        // 프론트로 boolean 값 반환
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/profile")
    public  ResponseEntity<Boolean> findUserToAuthServer(@RequestBody AuthServerProfileRequest profileRequest){
        boolean result= coreBankService.verifyProfile(profileRequest);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/email/pin")
    public ResponseEntity<Boolean> checkAuthEmailPinNumber(@RequestBody AuthServerEmailPinNumberRequest emailPinNumber){
        boolean result= coreBankService.checkEmailPinNumber(emailPinNumber);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/pin")
    public ResponseEntity<Boolean> checkAuthPinNumber(@RequestBody AuthServerPinNumberRequest pinNumber){
        boolean result= coreBankService.checkPinNumber(pinNumber);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/certificate/pin")
    public ResponseEntity<?> certificateCheckOnlyPinNumber(@MasterId Integer id,
                                                           @RequestBody AuthServerOnlyPinNumberRequest pinNumberRequest ){
        AuthServerPinNumberRequest request= presidentService.
                findByIdToPinNumberRequest(id,pinNumberRequest.getPinNumber());
        boolean result=coreBankService.checkPinNumber(request);
        if (!result)
            throw new CustomException(ErrorCode.INVALID_PIN_NUMBER);
        return ResponseEntity.ok("성공했습니다.");
    }
}
