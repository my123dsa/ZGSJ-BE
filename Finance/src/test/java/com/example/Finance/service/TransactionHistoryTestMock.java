package com.example.Finance.service;

import com.example.Finance.dto.TransactionHistoryRequest;
import com.example.Finance.dto.TransactionHistoryResponse;
import com.example.Finance.error.ErrorDTO;
import com.example.Finance.error.GlobalExceptionHandler;
import com.example.Finance.feign.CoreBankFeign;
import feign.FeignException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionHistoryTestMock {

    @Mock
    private CoreBankFeign coreBankFeign;

    @InjectMocks
    private TransactionHistoryService transactionHistoryService;

    @Test
    @DisplayName("빈 list는 성공")
    public void SuccessFeign() {
        // given
        TransactionHistoryRequest transactionHistoryRequest = new TransactionHistoryRequest("testaccount", "testcode");
        List<TransactionHistoryResponse> transactionHistoryResponseList = new LinkedList<>();

        when(coreBankFeign.getTransactionHistoryList(
                any(TransactionHistoryRequest.class),
                eq(2024),
                eq(11)))
                .thenReturn(transactionHistoryResponseList);

        // when
        List<TransactionHistoryResponse> result =
                transactionHistoryService.getYearMonthlyTransactions(transactionHistoryRequest, 2024, 11);

        // then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("1개 return")
    public void SuccessFeignOne() {
        TransactionHistoryRequest transactionHistoryRequest = new TransactionHistoryRequest("testaccount", "testcode");
        List<TransactionHistoryResponse> transactionHistoryResponseList = new LinkedList<>();
        transactionHistoryResponseList.add(new TransactionHistoryResponse("2024-03-13", 10000, true, "입금", "테스트임"));

        when(coreBankFeign.getTransactionHistoryList(transactionHistoryRequest, 2024, 11))
                .thenReturn(transactionHistoryResponseList);

        List<TransactionHistoryResponse> result =
                transactionHistoryService.getYearMonthlyTransactions(transactionHistoryRequest, 2024, 11);

        assertThat(result.get(0).getClassificationName()).isEqualTo("테스트임");

    }

    @Test
    @DisplayName("코어뱅킹 확인 결과, 해당 사용자 없음")
    public void FailNoAccountFeign() {
        TransactionHistoryRequest transactionHistoryRequest = new TransactionHistoryRequest("wrongaccount", "testcode");

        when(coreBankFeign.getTransactionHistoryList(
                any(TransactionHistoryRequest.class),
                eq(2024),
                eq(11)))
                .thenThrow(FeignException.errorStatus("", Response.builder()
                        .status(404)
                        .request(Request.create(Request.HttpMethod.GET, "", Collections.emptyMap(), new byte[0], Charset.defaultCharset()))
                        .body("{\"code\":\"ACCOUNT_NOT_FOUND\",\"message\":\"존재하지 않는 사용자의 계좌입니다\"}".getBytes())
                        .build()));

        assertThatThrownBy(() ->
                transactionHistoryService.getYearMonthlyTransactions(transactionHistoryRequest, 2024, 11))
                .isInstanceOf(FeignException.class)
                .hasMessageContaining("존재하지 않는 사용자의 계좌입니다");
    }
    
    @Test
    @DisplayName("feign통신 과정에서 오류 발생")
    public void feignwrong()
    {
        TransactionHistoryRequest request = new TransactionHistoryRequest();
        Integer year = 2023;
        Integer month = 5;
        String errorDetail = "Bad request";

        Map<String, Collection<String>> headers = new HashMap<>();
        headers.put("Content-Type", Collections.singletonList("application/json"));

        Request request2 = Request.create(
                Request.HttpMethod.GET,
                "/transaction-history",
                headers,
                new byte[0],
                StandardCharsets.UTF_8,
                null
        );

        Mockito.when(coreBankFeign.getTransactionHistoryList(request, year, month))
                .thenThrow(new FeignException.BadRequest(errorDetail, request2, new byte[0], Collections.emptyMap()));

        FeignException exception = Assertions.assertThrows(FeignException.class, () -> {
            transactionHistoryService.getYearMonthlyTransactions(request, year, month);
        });

        Assertions.assertEquals("Bad request", exception.getMessage());
    }
}
