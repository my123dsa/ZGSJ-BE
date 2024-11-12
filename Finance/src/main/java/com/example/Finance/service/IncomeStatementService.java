package com.example.Finance.service;

import com.example.Finance.dto.IncomeStatementResponse;
import com.example.Finance.dto.TransactionHistoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncomeStatementService {

    public IncomeStatementResponse calculateStatement(List<TransactionHistoryResponse> transactions) {
        // 총 매출액 계산
        BigDecimal totalRevenue = calculateRevenue(transactions);
        log.info("총 매출액 계산" + totalRevenue);

        // 매출원가 계산
        BigDecimal costOfSales = calculateCostOfSales(transactions);
        log.info("매출 원가 계산" + costOfSales);

        // 판매관리비 계산
        BigDecimal operatingExpenses = calculateOperatingExpenses(transactions);
        log.info("판매관리비 계산" + operatingExpenses);

        // 매출총이익 계산
        BigDecimal grossProfit = totalRevenue.subtract(costOfSales);
        log.info("매출총이익 계산" + grossProfit);

        //영업이익
        BigDecimal operatingIncome = grossProfit.subtract(operatingExpenses);
        log.info("영업이익 계산" + operatingIncome);

        // 수익률 계산
        BigDecimal profitMargin = calculateProfitMargin(operatingIncome, totalRevenue);
        log.info("수익률 계산" + operatingIncome);

        return IncomeStatementResponse.of(
                totalRevenue, costOfSales,
                operatingExpenses, grossProfit,
                operatingIncome, profitMargin
        ) ;
    }

    private BigDecimal calculateRevenue(List<TransactionHistoryResponse> transactions) {
        return transactions.stream()
                .filter(TransactionHistoryResponse::getIsDeposit)
                .filter(t -> isRevenueClassification(t.getClassificationName()))
                .map(t -> new BigDecimal(t.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateCostOfSales(List<TransactionHistoryResponse> transactions) {
        return transactions.stream()
                .filter(t -> !t.getIsDeposit())
                .filter(t -> isCostOfSalesClassification(t.getClassificationName()))
                .map(t -> new BigDecimal(t.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateOperatingExpenses(List<TransactionHistoryResponse> transactions) {
        return transactions.stream()
                .filter(t -> !t.getIsDeposit())
                .filter(t -> isOperatingExpenseClassification(t.getClassificationName()))
                .map(t -> new BigDecimal(t.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //입금 내역(수익)
    private boolean isRevenueClassification(String classificationName) {
        return List.of(
                "일일 매출 입금",
                "카드 매출 정산금",
                "현금 매출 입금",
                "온라인 결제 정산",
                "배달앱 정산금",
                "정부지원금 입금"
        ).contains(classificationName);
    }

    //원재료 비용
    private boolean isCostOfSalesClassification(String classificationName) {
        return List.of(
                "거래처 대금 지급",
                "재료/원자재 구매"
        ).contains(classificationName);
    }

    //지출
    private boolean isOperatingExpenseClassification(String classificationName) {
        return List.of(
                "임대료/관리비 지급",
                "직원 급여 이체",
                "4대보험료 납부",
                "세금 납부",
                "공과금 납부",
                "식비",
                "지출 관련",
                "사무용품 구입",
                "업무용 장비 구매",
                "비품 구입",
                "광고/마케팅 비용",
                "배달앱 수수료 결제",
                "부가가치세 납부",
                "종합소득세 납부",
                "상가 보험료",
                "POS 사용료",
                "카드단말기 수수료"
        ).contains(classificationName);
    }

    //율(%)로 바꾸기
    private BigDecimal calculateProfitMargin(BigDecimal operatingIncome, BigDecimal totalRevenue) {
        if (totalRevenue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return operatingIncome
                .multiply(new BigDecimal("100"))
                .divide(totalRevenue, 2, RoundingMode.HALF_UP);
    }

}