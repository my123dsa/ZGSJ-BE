package com.example.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TransactionChartResponse {
    private List<TransactionHistoryResponse> sales; //매출
    private List<TransactionHistoryResponse> expenses; //지출
    private List<Long> monthlySales; //년도 별 월별 매출
    private Long totalSales; //월 별 총매출
    private Long totalExpenses; //월 별 총지출

    public static TransactionChartResponse createEmpty() {
        return new TransactionChartResponse(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(Collections.nCopies(12, 0L)),
                0L,
                0L
        );
    }

}
