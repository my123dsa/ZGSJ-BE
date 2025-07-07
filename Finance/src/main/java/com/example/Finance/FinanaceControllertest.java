package com.example.Finance;

import feign.Feign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@Slf4j
//@RequiredArgsConstructor
//public class FinanaceControllertest {
//    private final FeignWithCeo feignWithCeo;
//
//    @GetMapping("/test")
//    public String test1()
//    {
//        return "매출/지출부 테스트 완료";
//    }
//
//    @GetMapping("/financefeign")
//    String givestringtoemployee()
//    {
//        return "finance에서 employee에게 전달";
//    }
//
//    @GetMapping("/feign")
//    String takebyceo()
//    {
//        return feignWithCeo.requesttoCEO();
//    }
//}
