package com.example.Finance.service;

import com.example.Finance.dto.TransactionChartResponse;
import com.example.Finance.dto.TransactionHistoryRequest;
import com.example.Finance.dto.TransactionHistoryResponse;
import com.example.Finance.dto.TransactionHistoryWithCounterPartyResponse;
import com.example.Finance.feign.CoreBankFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionHistoryService {
    private final CoreBankFeign coreBankFeign;

    //차트용 데이터 받아오기
    public TransactionChartResponse getTransactionChartData(
            TransactionHistoryRequest transactionHistoryRequest,
            Integer year,
            Integer month
    ) {
        //데이터 받기(년+월)
        List<TransactionHistoryResponse> transactionHistoryResponses = getYearMonthlyTransactions(
                transactionHistoryRequest, year, month
        );

        //데이터 받기(년)
        List<TransactionHistoryResponse> transactionHistoryResponsesYear = getYearlyTransactions(
                transactionHistoryRequest, year
        );

        //차트 데이터 생성
        TransactionChartResponse transactionChartResponse = new TransactionChartResponse();

        transactionChartResponse.setSales(
                calculateMonthlyExpenses(transactionHistoryResponses)
        );
        transactionChartResponse.setExpenses(
                calculateMonthlyExpenses(transactionHistoryResponses)
        );

        Map<Integer, Long> monthlySalesMap = calculateMonthlySales(transactionHistoryResponsesYear);
        List<Long> monthlySalesData = new ArrayList<>(12);
        for (int i = 1; i <= 12; i++) {
            monthlySalesData.add(i - 1, monthlySalesMap.getOrDefault(i, 0L));
        }
        transactionChartResponse.setMonthlySales(monthlySalesData);

        transactionChartResponse.setTotalSales(
                calculateTotalSales(transactionHistoryResponses)
        );
        transactionChartResponse.setTotalExpenses(
                calculateTotalExpenses(transactionHistoryResponses)
        );

        return transactionChartResponse;
    }

    //요청받은 REQUEST에 따라, LIST로 거래 내역 받아오기
    public List<TransactionHistoryResponse> getYearMonthlyTransactions(
            TransactionHistoryRequest transactionHistoryRequest,
            Integer year,
            Integer month)
    {
        return coreBankFeign.getTransactionHistoryList(transactionHistoryRequest, year, month);
    }

    public List<TransactionHistoryWithCounterPartyResponse> getYearMonthlyTransactionsWithCounterPartyName(
            TransactionHistoryRequest transactionHistoryRequest,
            Integer year,
            Integer month)
    {
        return coreBankFeign.getTransactionHistoryYearSalesListWithCounterParty(transactionHistoryRequest, year, month);
    }

    public List<TransactionHistoryResponse> getYearlyTransactions(
            TransactionHistoryRequest transactionHistoryRequest,
            Integer year
    ) {
        return coreBankFeign.getTransactionHistoryYearSalesList(transactionHistoryRequest, year);
    }

    public Map<Integer, Long> calculateMonthlySales(
            List<TransactionHistoryResponse> transactionHistoryResponses
    ) {
        return transactionHistoryResponses.stream()
                .filter(TransactionHistoryResponse::getIsDeposit)
                .collect(Collectors.groupingBy(
                        tr -> Integer.valueOf(tr.getTransactionDate().split("-")[1]),
                        Collectors.summingLong(TransactionHistoryResponse::getAmount)
                ));
    }

    public List<TransactionHistoryResponse> calculateMonthlyExpenses(
            List<TransactionHistoryResponse> transactionHistoryResponses
    ) {
        return transactionHistoryResponses.stream()
                .filter(tr -> !tr.getIsDeposit())
                .collect(Collectors.toList());
    }

    public long calculateTotalSales(
            List<TransactionHistoryResponse> transactionHistoryResponses
    ) {
        return transactionHistoryResponses.stream()
                .filter(tr -> tr.getIsDeposit())
                .mapToLong(TransactionHistoryResponse::getAmount)
                .sum();
    }

    public long calculateTotalExpenses(
            List<TransactionHistoryResponse> transactionHistoryResponses
    ) {
        return transactionHistoryResponses.stream()
                .filter(tr -> !tr.getIsDeposit())
                .mapToLong(TransactionHistoryResponse::getAmount)
                .sum();
    }




}
