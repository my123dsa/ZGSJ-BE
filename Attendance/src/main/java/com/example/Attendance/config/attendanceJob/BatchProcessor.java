package com.example.Attendance.config.attendanceJob;

import com.example.Attendance.FeignWithCoreBank;
import com.example.Attendance.dto.batch.*;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.error.FeignExceptionHandler;
import com.example.Attendance.service.BigService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchProcessor {

    private final FeignWithCoreBank feignWithCoreBank;
    private final BigService bigService;
    private final BatchJobState batchJobState;
    private final FeignExceptionHandler handler;

    @Bean
    public ItemProcessor<BatchInputData, BatchOutputData> attendanceProcessor() {
        return item -> {
            try {
                CommuteSummary commuteSummary = batchJobState.getCommuteDuration(item.getSeId());
                //여기 들어오기전에 0인애들 필터링 하면 좋을 듯
                if (commuteSummary == null) {
                    return null;
                }
                bigService.calculate(item, commuteSummary);

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

}
