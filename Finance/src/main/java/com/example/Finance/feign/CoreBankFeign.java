package com.example.Finance.feign;

import com.example.Finance.dto.TransactionHistoryRequest;
import com.example.Finance.dto.TransactionHistoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "CoreBank", url = "http://localhost:3030")
public interface CoreBankFeign {

    @PostMapping("/bank/list")
    List<TransactionHistoryResponse> getTransactionHistoryList
            (@RequestBody TransactionHistoryRequest transactionHistoryRequest,
             @RequestParam Integer year,
             @RequestParam Integer month);
}