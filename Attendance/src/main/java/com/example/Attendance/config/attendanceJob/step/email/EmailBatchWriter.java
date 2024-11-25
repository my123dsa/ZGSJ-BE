package com.example.Attendance.config.attendanceJob.step.email;

import com.example.Attendance.UserFeignService;
import com.example.Attendance.config.attendanceJob.BatchService;
import com.example.Attendance.dto.batch.email.EmailOutputData;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class EmailBatchWriter {
    private final BatchService batchService;
    private final UserFeignService feignService;

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
