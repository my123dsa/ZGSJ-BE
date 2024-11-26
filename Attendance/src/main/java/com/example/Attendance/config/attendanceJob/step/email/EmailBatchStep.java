package com.example.Attendance.config.attendanceJob.step.email;

import com.example.Attendance.feign.UserFeignService;
import com.example.Attendance.service.batch.BatchService;
import com.example.Attendance.dto.batch.email.EmailInputData;
import com.example.Attendance.dto.batch.email.EmailOutputData;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.service.EmailService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor

public class EmailBatchStep {

    private final EmailBatchState emailBatchState;
    private final BatchService batchService;
    private final EmailService emailService;
    private final UserFeignService feignService;


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

    @Bean
    @StepScope
    public ItemWriter<EmailOutputData> emailWriter() {
        return chunk -> {
            List<Integer> batchIds = chunk.getItems().stream()
                    .filter(EmailOutputData::getResult)
                    .map(EmailOutputData::getBatchId)
                    .toList();
            List<Integer> maskIds= chunk.getItems().stream()
                    .filter(EmailOutputData::getIsMask)
                    .map(EmailOutputData::getSeId)
                    .toList();
            batchService.updateEmailResultByIds(batchIds);
            if (!maskIds.isEmpty()) {
                try {
                    Boolean maskingResult =feignService.sendMaskingIds(maskIds);
                    if (!maskingResult){
                        throw new CustomException(ErrorCode.UPDATE_FAIL);
                    }
                }catch (FeignException fe){
                    throw new CustomException(ErrorCode.Feign_Error);
                }catch (Exception e){
                    throw new CustomException(ErrorCode.SERVER_ERROR);
                }
            }
        };
    }

}
