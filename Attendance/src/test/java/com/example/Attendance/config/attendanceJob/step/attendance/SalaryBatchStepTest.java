package com.example.Attendance.config.attendanceJob.step.attendance;

import com.example.Attendance.dto.batch.BatchInputData;
import com.example.Attendance.dto.batch.BatchOutputData;
import com.example.Attendance.dto.batch.CommuteSummary;
import com.example.Attendance.feign.FeignWithCoreBank;
import com.example.Attendance.service.batch.CalculateService;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalaryBatchStepTest {

    @Mock  // @MockBean 대신 @Mock 사용
    private SalaryBatchState salaryBatchState;

    @Mock
    private FeignWithCoreBank feignWithCoreBank;

    @Mock
    private CalculateService calculateService;

    @InjectMocks  // @Autowired 대신 @InjectMocks 사용
    private SalaryBatchStep salaryBatchStep;

    private BatchInputData testInputData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mockito 초기화 추가
        testInputData = new BatchInputData(
                1,
                "111122223333",
                "004",
                (byte) 1,
                "444455556666",
                "004",
                3000000L,
                "홍길동",
                "김사장",
                "employee@test.com",
                LocalDate.of(1990, 1, 1),
                "01012345678",
                "president@test.com"
        );
    }

    @Test
    @DisplayName("자동이체중 FeignException 발생")
    public void 자동이체중_금융서버_Exception() throws Exception {
        // given
        when(salaryBatchState.getCommuteDuration(any()))
                .thenReturn(new CommuteSummary(1,1l,1));
        when(feignWithCoreBank.automaticTransfer(any()))
                .thenThrow(FeignException.class);

        // when
        ItemProcessor<BatchInputData, BatchOutputData> processor = salaryBatchStep.salaryProcessor();
        BatchOutputData result = processor.process(testInputData);

        // then
        assertNull(result);
    }
}