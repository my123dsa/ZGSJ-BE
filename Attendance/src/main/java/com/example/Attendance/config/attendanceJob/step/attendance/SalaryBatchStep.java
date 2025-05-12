package com.example.Attendance.config.attendanceJob.step.attendance;

import com.example.Attendance.dto.batch.*;
import com.example.Attendance.error.ErrorDTO;
import com.example.Attendance.error.FeignExceptionHandler;
import com.example.Attendance.error.log.ErrorType;
import com.example.Attendance.feign.FeignWithCoreBank;
import com.example.Attendance.model.Batch;
import com.example.Attendance.service.StoreEmployeeService;
import com.example.Attendance.service.batch.BatchService;
import com.example.Attendance.service.batch.CalculateService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SalaryBatchStep {

    private final SalaryBatchReader salaryBatchReader;
    private final StoreEmployeeService storeEmployeeService;
    private final BatchService batchService;
    private final FeignWithCoreBank feignWithCoreBank;
    private final CalculateService calculateService;
    private final FeignExceptionHandler handler;

    @Bean("salaryReader")
    public SalaryBatchReader salaryReader() {
        return salaryBatchReader;
    }

    @Bean("salaryProcessor")
    public ItemProcessor<BatchInputData, BatchOutputData> salaryProcessor() {
        return item -> {
            try {
                CommuteSummary commuteSummary = salaryBatchReader.getCommuteSummary(item.getSeId());
                if (commuteSummary == null) {
                    return null;
                }
                calculateService.calculate(item, commuteSummary);

                TransferRequest request = TransferRequest.from(item);
                TransferRequest adminRequest = TransferRequest.fromForAdmin(request);

                TransferResponse response = feignWithCoreBank.automaticTransfer(request);

                try {
                    TransferResponse responseAdmin = feignWithCoreBank.automaticTransfer(adminRequest);  // 수수료 이체
                    return BatchOutputData.of(response, item, true);
                } catch (Exception fe) {
                    log.error("수수료 이체 실패 - amount={}, error={}", adminRequest.getAmount(), fe.getMessage());
                    return BatchOutputData.of(response, item, false);
                }
            } catch (FeignException fe) {
                log.error("금융서버 통신 실패 - president_account={}, employee_account={}, error={}, type={}",
                        item.getFromAccount(), item.getToAccount(), fe.getMessage(), ErrorType.FEIGN_EXCEPTION.name());
                ErrorDTO dto = handler.feToErrorDTO(fe);
                return BatchOutputData.ofFail(item, dto.getCode());

            } catch (Exception e) {
                log.error("자동이체 처리 실패 - president_account={}, employee_account={}, error={}, type={}",
                        item.getFromAccount(), item.getToAccount(), e.getMessage(), ErrorType.INTERNAL_ERROR.name());
                return BatchOutputData.ofFail(item, "서버 오류");
            }
        };
    }

    @Bean("salaryWriter")
    public ItemWriter<BatchOutputData> salaryWriter() {
        return chunk -> {
            List<Batch> batches = chunk.getItems().stream()
                    .map(BatchOutputData::ToBatchEntity)
                    .toList();
            batchService.saveAll(batches);

            List<Integer> ids = chunk.getItems().stream()
                    .filter(BatchOutputData::getIsMask)
                    .map(BatchOutputData::getSeId)
                    .toList();

            storeEmployeeService.updateEmployeeType(ids);
            log.info("급여 이체 결과 {} 건 저장 완료", chunk.size());
        };
    }
}
