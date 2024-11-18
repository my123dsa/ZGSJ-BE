package com.example.Attendance.controller;

import com.example.Attendance.dto.PayStatementResponse;
import com.example.Attendance.service.PayStatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/paystatement")
@RestController
@RequiredArgsConstructor
public class PayStatementController {

    private final PayStatementService payStatementService;

    @GetMapping
    public ResponseEntity<List<PayStatementResponse>> getPayStatementResponse
            (
                    @RequestParam Integer storeid,
                    @RequestParam("year") Integer year,
                    @RequestParam("month") Integer month
            )
    {
        return ResponseEntity.ok(payStatementService.getPayStatementMonthlyYearly(storeid,year,month));
    }
}
