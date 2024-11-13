package com.example.Attendance;

import com.example.Attendance.dto.TransferRequest;
import com.example.Attendance.dto.TransferResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EmployeeTestController {

    private final FeignwithFinance feignwithFinance;
    private final FeignWithCoreBank feignWithCoreBank;

    @GetMapping("/test")
    public String employeetest()
    {
        return "직원 근태관리 테스트";
    }

    @GetMapping("/employeefeign")
    String feigntestemployeeceo(){
        return "feign ceo-employee 테스트";
    } //ceo에서 호출하면, 이 controlelr로 보내요

//    @GetMapping("/feign")
//    String feigntestwithfinance(){
//        return feignwithFinance.feigntestemployeefinance();
//    }
//    @PostMapping("/feign")
//    TransferResponse feigntestwithfinancepost(TransferRequest transferRequest){
//        log.info("Transfer request received: {}", transferRequest);
//        return feignWithCoreBank.automaticTransfer(transferRequest);
//    }
}
