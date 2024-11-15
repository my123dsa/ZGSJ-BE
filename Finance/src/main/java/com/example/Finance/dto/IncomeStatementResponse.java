package com.example.Finance.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IncomeStatementResponse {
    private final BigDecimal totalRevenue;      // 총 매출액
    private final BigDecimal costOfSales;       // 매출원가
    private final BigDecimal grossProfit;       // 매출총이익
    private final BigDecimal operatingExpenses; // 판매관리비
    private final BigDecimal operatingIncome;   // 영업이익
    private final BigDecimal profitMargin;      // 수익률

    public static IncomeStatementResponse of(
            BigDecimal totalRevenue,
            BigDecimal costOfSales,
            BigDecimal grossProfit,
            BigDecimal operatingExpenses,
            BigDecimal operatingIncome,
            BigDecimal profitMargin
    ) {
        return new IncomeStatementResponse(
                totalRevenue,
                costOfSales,
                grossProfit,
                operatingExpenses,
                operatingIncome,
                profitMargin
        );
    }
}