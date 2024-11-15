package com.example.Finance.feign;

import com.example.Finance.dto.TransactionHistoryRequest;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CoreBankFeignTest {

    //feignexcpeiton이 잘 발생하는지!

    @Mock
    CoreBankFeign coreBankFeign;

    @Test
    @DisplayName("OpenFeign 통신 중 Bad Request 예외 발생")
    public void testGetTransactionHistoryListBadRequest() {

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

        TransactionHistoryRequest request = new TransactionHistoryRequest();
        Integer year = 2023;
        Integer month = 5;
        String errorDetail = "Bad request";

        Mockito.when(coreBankFeign.getTransactionHistoryList(request, year, month))
                .thenThrow(new FeignException.BadRequest(errorDetail, request2, new byte[0], Collections.emptyMap()));

        FeignException exception = Assertions.assertThrows(FeignException.class, () -> {
            coreBankFeign.getTransactionHistoryList(request, year, month);
        });

        Assertions.assertTrue(exception instanceof FeignException.BadRequest);
        Assertions.assertEquals(errorDetail, exception.getMessage());
    }

    @Test
    @DisplayName("OpenFeign 통신 중 Unauthorized 예외 발생")
    public void testGetTransactionHistoryListUnauthorized() {

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

        TransactionHistoryRequest request = new TransactionHistoryRequest();
        Integer year = 2023;
        Integer month = 5;
        String errorDetail = "Unauthorized";

        Mockito.when(coreBankFeign.getTransactionHistoryList(request, year, month))
                .thenThrow(new FeignException.Unauthorized(errorDetail, request2, new byte[0], Collections.emptyMap()));

        FeignException exception = Assertions.assertThrows(FeignException.class, () -> {
            coreBankFeign.getTransactionHistoryList(request, year, month);
        });

        Assertions.assertTrue(exception instanceof FeignException.Unauthorized);
        Assertions.assertEquals(errorDetail, exception.getMessage());
    }


    @Test
    @DisplayName("OpenFeign 통신 중 Not Found 예외 발생")
    public void testGetTransactionHistoryListNotFound() {

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

        TransactionHistoryRequest request = new TransactionHistoryRequest();
        Integer year = 2023;
        Integer month = 5;
        String errorDetail = "Not found";

        Mockito.when(coreBankFeign.getTransactionHistoryList(request, year, month))
                .thenThrow(new FeignException.NotFound(errorDetail, request2, new byte[0], Collections.emptyMap()));

        FeignException exception = Assertions.assertThrows(FeignException.class, () -> {
            coreBankFeign.getTransactionHistoryList(request, year, month);
        });

        Assertions.assertTrue(exception instanceof FeignException.NotFound);
        Assertions.assertEquals(errorDetail, exception.getMessage());
    }

    @Test
    @DisplayName("OpenFeign 통신 중 Internal Server Error 예외 발생")
    public void testGetTransactionHistoryListInternalServerError() {

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

        TransactionHistoryRequest request = new TransactionHistoryRequest();
        Integer year = 2023;
        Integer month = 5;
        String errorDetail = "Internal server error";

        Mockito.when(coreBankFeign.getTransactionHistoryList(request, year, month))
                .thenThrow(new FeignException.InternalServerError(errorDetail, request2, new byte[0], Collections.emptyMap()));

        FeignException exception = Assertions.assertThrows(FeignException.class, () -> {
            coreBankFeign.getTransactionHistoryList(request, year, month);
        });

        Assertions.assertTrue(exception instanceof FeignException.InternalServerError);
        Assertions.assertEquals(errorDetail, exception.getMessage());
    }

}