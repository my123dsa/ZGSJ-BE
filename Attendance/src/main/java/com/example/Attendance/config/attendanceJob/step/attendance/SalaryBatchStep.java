package com.example.Attendance.config.attendanceJob.step.attendance;

import com.example.Attendance.feign.FeignWithCoreBank;
import com.example.Attendance.model.Batch;
import com.example.Attendance.repository.BatchRepository;
import com.example.Attendance.dto.batch.*;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.error.FeignExceptionHandler;
import com.example.Attendance.repository.StoreEmployeeRepository;
import com.example.Attendance.service.batch.CalculateService;
import com.example.Attendance.service.CommuteService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor

public class SalaryBatchStep {

    private final SalaryBatchState salaryBatchState;
    private final StoreEmployeeRepository storeEmployeeRepository;
    private final CommuteService commuteService;
    private final BatchRepository batchRepository;
    private final FeignWithCoreBank feignWithCoreBank;
    private final CalculateService calculateService;
    private final FeignExceptionHandler handler;

    @Bean
    public ItemReader<BatchInputData> salaryReader() {
        return () -> {
            try {
                if (salaryBatchState.getEmployees() == null) {
                    salaryBatchState.setLocalDate();
                    salaryBatchState.setEmployees(storeEmployeeRepository
//                            .findAllBatchInputDataByPaymentDate(20)); //테스트용
                            .findAllBatchInputDataByPaymentDate(salaryBatchState.getLocalDate().getDayOfMonth()));
                    if (salaryBatchState.getEmployees().isEmpty()) {
                        log.info("처리할 직원 데이터가 없습니다.");
                        return null;
                    }
                    salaryBatchState.setCommutes(commuteService.
                            findAllByCommuteDateBetween(salaryBatchState.getEmployeeIds(), salaryBatchState.getLocalDate()));
                }

                // 데이터 반환
                if (salaryBatchState.getCurrentIndex() < salaryBatchState.getEmployees().size()) {
                    BatchInputData bid = salaryBatchState.getEmployees()
                            .get(salaryBatchState.getCurrentIndex());

                    salaryBatchState.indexUp();
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
    public ItemProcessor<BatchInputData, BatchOutputData> salaryProcessor() {
        return item -> {
            try {
                CommuteSummary commuteSummary = salaryBatchState.getCommuteDuration(item.getSeId());
                //여기 들어오기전에 0인애들 필터링 하면 좋을 듯
                if (commuteSummary == null) {
                    return null;
                }
                calculateService.calculate(item, commuteSummary);

                TransferRequest request = TransferRequest.from(item);

                TransferResponse response = feignWithCoreBank.automaticTransfer(request);

                return BatchOutputData.of(response, item);
            } catch (FeignException fe) {
//                if (dto.getCode().equals("ACCOUNT_NOT_FOUND"))
//                    throw new CustomException(ErrorCode.ACCOUNT_NOT_FOUND);
//                ErrorDTO dto =  handler.feToErrorDTO(fe);
//                if (dto.getCode().equals("INSUFFICIENT_BALANCE"))
//                    throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
//                // 이것들은
//                throw new CustomException(ErrorCode.RETRY_BATCH_STEP);
                return null;
            } catch (Exception e) {
                log.error("송금 내부 처리 실패 - employee: {}, error: {}",
                        item.getSeId(), e.getMessage());
                throw new CustomException(ErrorCode.SERVER_ERROR);
            }
        };
    }

    @StepScope
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ItemWriter<BatchOutputData> salaryWriter() {
        return chunk -> {
            List<Batch> batches= chunk.getItems().stream().map(BatchOutputData::ToBatchEntity).toList();
            batchRepository.saveAll(batches);
            log.info("급여 이체 결과 {} 건 저장 완료", chunk.size());
        };
    }
}
