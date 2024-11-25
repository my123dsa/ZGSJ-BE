package com.example.Attendance.config.attendanceJob.step.email;

import com.example.Attendance.dto.batch.email.EmailInputData;
import com.example.Attendance.dto.batch.email.EmailOutputData;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class EmailBatchProcessor {
    private final EmailService emailService;

    @Bean
    @StepScope
    public ItemProcessor<EmailInputData, EmailOutputData> emailProcessor() {
        return item -> {
            try {
                log.info("결과 : {} {} {}",item.getBankResult(),item.getPdfResult(),item.getBatchId());
                if (item.getPdfResult() && item.getBankResult()){
                    emailService.sendPayStatement(item.getEmployeeEmail(),item.getUrl());
                }else if (item.getBankResult()) {
                    emailService.sendPdfFail(item.getPresidentEmail(), item.getName(), item.getIssuanceDate(), item.getMessage());
                }else
                    emailService.sendBankFail(item.getPresidentEmail(),item.getName(),item.getIssuanceDate(),item.getMessage());
                return EmailOutputData.of(item.getSeId(),item.getBatchId(),true,item.getIsMask());
            } catch (Exception e) {
                throw new CustomException(ErrorCode.SERVER_ERROR);
            }
        };
    }

}
