package com.example.User.feign;

import com.example.User.dto.businessnumber.BusinessNumberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "CoreBank", url = "http://localhost:3030")
public interface CoreBankFeign {

    @PostMapping("/businesscheck")
    BusinessNumberResponse checkBusinessNumber(@RequestBody String businessNumber);
}
