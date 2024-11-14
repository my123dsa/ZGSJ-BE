package com.example.Finance.service;

import com.example.Finance.dto.IncomeStatementResponse;
import com.example.Finance.dto.TransactionHistoryResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IncomeStatementServiceTest {

    @Autowired
    private IncomeStatementService incomeStatementService;

    @Test
    void testCalCulateIncomeStatement()
    {
        List<TransactionHistoryResponse> transactions = new ArrayList<>();
        transactions.add(new TransactionHistoryResponse("2023-05-01", 10000, true, "Revenue","일일 매출 입금"));
        transactions.add(new TransactionHistoryResponse("2023-05-02", 5000, true,  "Revenue","카드 매출 정산금"));
        transactions.add(new TransactionHistoryResponse("2023-05-03", 2000, false,  "Cost of Sales","거래처 대금 지급"));
        transactions.add(new TransactionHistoryResponse("2023-05-05", 500, false,  "Operating Expenses","직원 급여 이체"));
        transactions.add(new TransactionHistoryResponse("2023-05-04", 1000, false,  "Operating Expenses", "임대료/관리비 지급"));
        IncomeStatementResponse statement = incomeStatementService.calculateStatement(transactions);

        Assertions.assertEquals(new BigDecimal("15000"), statement.getTotalRevenue());
        Assertions.assertEquals(new BigDecimal("2000"), statement.getCostOfSales());
        Assertions.assertEquals(new BigDecimal("13000"), statement.getGrossProfit());
        Assertions.assertEquals(new BigDecimal("1500"), statement.getOperatingExpenses());
        Assertions.assertEquals(new BigDecimal("11500"), statement.getOperatingIncome());
        Assertions.assertEquals(new BigDecimal("76.67"), statement.getProfitMargin());
    }


}