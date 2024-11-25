package com.example.Attendance.config.attendanceJob.step.email;

import com.example.Attendance.config.attendanceJob.BatchService;
import com.example.Attendance.dto.batch.email.EmailInputData;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Slf4j
@Configuration
@RequiredArgsConstructor

public class EmailBatchReader {

    private final EmailBatchState emailBatchState;
    private final BatchService batchService;

    @Bean
    @StepScope
    public ItemReader<EmailInputData> emailReader() {
        emailBatchState.findAllByLocalDate(batchService.findAllByLocalDate(LocalDate.now()));
        return () -> {
            try {
                if (emailBatchState.getIndex()<emailBatchState.getBatches().size()) {
                    EmailInputData inputData= emailBatchState.getBatches().get(emailBatchState.getIndex());
                    emailBatchState.upIndex();
                    return inputData;
                }
                log.info("모든 직원 데이터 처리 완료");
                return null;
            } catch (Exception e) {
                throw new CustomException(ErrorCode.API_SERVER_ERROR);
            }
        };
    }
}
