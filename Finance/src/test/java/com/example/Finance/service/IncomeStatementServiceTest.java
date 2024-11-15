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


    @Test
    void testCalculateRevenue() {
        List<TransactionHistoryResponse> transactions = new ArrayList<>();
        transactions.add(new TransactionHistoryResponse("2023-05-01", 10000, true, "일일 매출 입금", "Revenue"));
        transactions.add(new TransactionHistoryResponse("2023-05-02", 5000, true, "카드 매출 정산금", "Revenue"));
        transactions.add(new TransactionHistoryResponse("2023-05-03", 2000, false, "거래처 대금 지급", "Cost of Sales"));

        BigDecimal revenue = incomeStatementService.calculateRevenue(transactions);

        //매출
        Assertions.assertEquals(new BigDecimal("15000"), revenue);
    }

    @Test
    void testCalculateCostOfSales() {

        List<TransactionHistoryResponse> transactions = new ArrayList<>();
        transactions.add(new TransactionHistoryResponse("2023-05-03", 2000, false, "거래처 대금 지급", "Cost of Sales"));
        transactions.add(new TransactionHistoryResponse("2023-05-04", 1000, false, "재료/원자재 구매", "Cost of Sales"));


        BigDecimal costOfSales = incomeStatementService.calculateCostOfSales(transactions);

        Assertions.assertEquals(new BigDecimal("3000"), costOfSales);
    }

    @Test
    void testCalculateOperatingExpenses() {

        //판매 관리비
        List<TransactionHistoryResponse> transactions = new ArrayList<>();
        transactions.add(new TransactionHistoryResponse("2023-05-04", 1000, false, "임대료/관리비 지급", "Operating Expenses"));
        transactions.add(new TransactionHistoryResponse("2023-05-05", 500, false, "직원 급여 이체", "Operating Expenses"));

        BigDecimal operatingExpenses = incomeStatementService.calculateOperatingExpenses(transactions);

        Assertions.assertEquals(new BigDecimal("1500"), operatingExpenses);
    }

    @Test
    void testCalculateProfitMargin() {

        BigDecimal operatingIncome = new BigDecimal("11500");
        BigDecimal totalRevenue = new BigDecimal("15000");

        BigDecimal profitMargin = incomeStatementService.calculateProfitMargin(operatingIncome, totalRevenue);

        Assertions.assertEquals(new BigDecimal("76.67"), profitMargin.setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @Test
    void testIsRevenueClassification() {
        // Arrange
        String revenueClassification = "일일 매출 입금";
        String nonRevenueClassification = "거래처 대금 지급";

        // Act
        boolean isRevenue = incomeStatementService.isRevenueClassification(revenueClassification);
        boolean isNotRevenue = incomeStatementService.isRevenueClassification(nonRevenueClassification);

        // Assert
        Assertions.assertTrue(isRevenue);
        Assertions.assertFalse(isNotRevenue);
    }

    @Test
    void testIsCostOfSalesClassification() {
        // Arrange
        String costOfSalesClassification = "거래처 대금 지급";
        String nonCostOfSalesClassification = "일일 매출 입금";

        // Act
        boolean isCostOfSales = incomeStatementService.isCostOfSalesClassification(costOfSalesClassification);
        boolean isNotCostOfSales = incomeStatementService.isCostOfSalesClassification(nonCostOfSalesClassification);

        // Assert
        Assertions.assertTrue(isCostOfSales);
        Assertions.assertFalse(isNotCostOfSales);
    }

    @Test
    void testIsOperatingExpenseClassification() {
        // Arrange
        String operatingExpenseClassification = "임대료/관리비 지급";
        String nonOperatingExpenseClassification = "일일 매출 입금";

        // Act
        boolean isOperatingExpense = incomeStatementService.isOperatingExpenseClassification(operatingExpenseClassification);
        boolean isNotOperatingExpense = incomeStatementService.isOperatingExpenseClassification(nonOperatingExpenseClassification);

        // Assert
        Assertions.assertTrue(isOperatingExpense);
        Assertions.assertFalse(isNotOperatingExpense);
    }


}