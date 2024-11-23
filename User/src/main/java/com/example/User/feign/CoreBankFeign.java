package com.example.User.feign;

import com.example.User.dto.authserver.AuthServerEmailPinNumber;
import com.example.User.dto.authserver.AuthServerPinNumber;
import com.example.User.dto.businessnumber.BusinessNumberResponse;
import com.example.User.dto.corebank.AccountCheckRequest;
import com.example.User.dto.authserver.AuthServerProfileRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "CoreBank", url = "http://localhost:3030")
public interface CoreBankFeign {

    @PostMapping("/businesscheck")
    BusinessNumberResponse checkBusinessNumber(@RequestBody String businessNumber);

    @PostMapping("/bank/verify-account")
    boolean verifyAccount(@RequestBody AccountCheckRequest accountCheckRequest);

    @PostMapping("/authentication/profile/check")
    boolean verifyProfile(@RequestBody AuthServerProfileRequest profileRequest);

    @PostMapping("/authentication/email/pincheck")
    boolean checkEmailPinNumber(@RequestBody AuthServerEmailPinNumber emailPinNumber);

    @PostMapping("/authentication/pincheck")
    boolean checkPinNumber(@RequestBody AuthServerPinNumber checkPinNumber);
}
