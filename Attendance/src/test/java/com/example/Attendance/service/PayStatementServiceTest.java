package com.example.Attendance.service;

import com.example.Attendance.dto.PayStatementResponse;
import com.example.Attendance.repository.PayStatementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class PayStatementServiceTest {

    @Mock
    private PayStatementRepository payStatementRepository;

    @InjectMocks
    private PayStatementService payStatementService;

    @Test
    void testGetPayStatementMonthlyYearly() {
        Integer storeId = 1;
        Integer year = 2024;
        Integer month = 8;

        PayStatementResponse response1 = new PayStatementResponse(1, "BANK001", "ACC001", 5000, LocalDate.of(2024, 8, 1));
        PayStatementResponse response2 = new PayStatementResponse(2, "BANK002", "ACC002", 7500, LocalDate.of(2024, 8, 15));
        List<PayStatementResponse> mockResponses = List.of(response1, response2);

        Mockito.when(payStatementRepository.findPayStatementResponsesByStoreAndDateWithFetch(storeId, year, month))
                .thenReturn(mockResponses);

        List<PayStatementResponse> responses = payStatementService.getPayStatementMonthlyYearly(storeId, year, month);

        assertThat(responses).hasSize(2);

        PayStatementResponse responseOne = responses.get(0);
        assertThat(responseOne.getPayStatementId()).isEqualTo(1);
        assertThat(responseOne.getAccountNumber()).isEqualTo("ACC001");
        assertThat(responseOne.getAmount()).isEqualTo(5000);

        PayStatementResponse responseTwo = responses.get(1);
        assertThat(responseTwo.getPayStatementId()).isEqualTo(2);
        assertThat(responseTwo.getAccountNumber()).isEqualTo("ACC002");
        assertThat(responseTwo.getAmount()).isEqualTo(7500);
    }
}