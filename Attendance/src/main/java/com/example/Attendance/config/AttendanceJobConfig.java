package com.example.Attendance.config;

import com.example.Attendance.FeignWithCoreBank;
import com.example.Attendance.dto.*;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.model.PayStatement;
import com.example.Attendance.repository.PayStatementRepository;
import com.example.Attendance.repository.StoreEmployeeRepository;
import com.example.Attendance.service.BigService;
import com.example.Attendance.service.CommuteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AttendanceJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final StoreEmployeeRepository storeEmployeeRepository;
    private final PayStatementRepository payStatementRepository;
    private final BatchJobState batchJobState;
    private final FeignWithCoreBank feignWithCoreBank;
    private final CommuteService commuteService;
    private final BigService bigService;

    @Bean
    public Job attendanceJob() {
        return new JobBuilder("automaticTransferJob", jobRepository)
                .start(attendanceStep())
                .build();
    }

    @Bean
    public Step attendanceStep() {
        return new StepBuilder("automaticTransferStep", jobRepository)
                .<BatchInputData, BatchOutputData>chunk(3, transactionManager)
                .reader(attendanceReader())       // 데이터 읽기
                .processor(attendanceProcessor()) // 데이터 처리
                .writer(attendanceWriter())       // 데이터 쓰기
                .build();
    }

    @Bean
    public ItemReader<BatchInputData> attendanceReader() {
        return () -> {
            try {
                if (batchJobState.getEmployees() == null) {
                    batchJobState.setLocalDate();
                    batchJobState.setEmployees(storeEmployeeRepository
                            .findAllBatchInputDataByPaymentDate(batchJobState.getLocalDate().getDayOfMonth()));
                    if (batchJobState.getEmployees().isEmpty()) {
                        log.info("처리할 직원 데이터가 없습니다.");
                        return null;
                    }
                    batchJobState.setCommutes(commuteService.
                            findAllByCommuteDateBetween(batchJobState.getEmployeeIds(), batchJobState.getLocalDate()));
                }

                // 데이터 반환
                if (batchJobState.getCurrentIndex() < batchJobState.getEmployees().size()) {
                    BatchInputData bid = batchJobState.getEmployees()
                            .get(batchJobState.getCurrentIndex());

                    batchJobState.indexUp();
                    return bid;
                }

                log.info("모든 직원 데이터 처리 완료");
                return null;
            } catch (Exception e) {
                log.error("데이터 읽기 실패: {}", e.getMessage(), e);
                throw new CustomException(ErrorCode.API_SERVER_ERROR);
            }
        };
    }

    @Bean
    public ItemProcessor<BatchInputData, BatchOutputData> attendanceProcessor() {
        return item -> {
            try {
                Long commuteDuration = batchJobState.getCommuteDuration(item.getSeId());
                BatchInputDataWithAllowance bidwa = bigService.calculate(item, commuteDuration);

                TransferRequest request = TransferRequest.from(bidwa.getTotal() - bidwa.getCharge(), item);

                TransferResponse response = feignWithCoreBank.automaticTransfer(request);
                log.info("메시지 입니다 {}",response.getMessage());
                log.info("값 {} : {} : {} : {}",bidwa.getTotal(),bidwa.getAllowance(),bidwa.getCharge(),bidwa.getSalary());
                if (response.getStatus() == 200) {
                    return BatchOutputData.of(bidwa, response, item.getSeId());
                }
                //다른 에러 처리 필요
                return null;
            } catch (Exception e) {
                log.error("송금 처리 실패 - employee: {}, error: {}",
                        item.getSeId(), e.getMessage());
                return null;
            }
        };
    }

    @Bean
    public ItemWriter<BatchOutputData> attendanceWriter() {
        return chunk -> {
            List<PayStatement> payStatements = chunk.getItems().stream()
                    .map(BatchOutputData::toEntity)
                    .collect(Collectors.toList());

            payStatementRepository.saveAll(payStatements);
            log.info("급여 이체 결과 {} 건 저장 완료", chunk.size());
        };
    }
}