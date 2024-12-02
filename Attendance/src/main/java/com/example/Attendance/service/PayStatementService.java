package com.example.Attendance.service;

import com.example.Attendance.dto.PayStatementResponse;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.model.PayStatement;
import com.example.Attendance.repository.PayStatementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PayStatementService {

    private final PayStatementRepository payStatementRepository;

    public List<PayStatementResponse> getPayStatementMonthlyYearly(
            Integer storeId, Integer year, Integer month
    ) {
        return payStatementRepository.findPayStatementResponsesByStoreAndDateWithFetch(storeId, year, month);

    }

    public String getPayStatementUrl(Integer payStatementId) {
        return payStatementRepository.findPayStatementURL(payStatementId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_PAY_STATEMENT)
        );
    }
    public void saveAll(List<PayStatement> payStatements){
        payStatementRepository.saveAll(payStatements);
    }
}
