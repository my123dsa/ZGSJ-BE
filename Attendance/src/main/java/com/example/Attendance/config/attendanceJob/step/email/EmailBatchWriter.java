package com.example.Attendance.config.attendanceJob.step.email;

import com.example.Attendance.config.attendanceJob.BatchRepository;
import com.example.Attendance.config.attendanceJob.BatchService;
import com.example.Attendance.dto.batch.BatchOutputData;
import com.example.Attendance.dto.batch.email.EmailOutputData;
import com.example.Attendance.model.PayStatement;
import com.example.Attendance.repository.PayStatementRepository;
import com.example.Attendance.service.EmailService;
import com.example.Attendance.service.GCPService;
import com.example.Attendance.service.PayStatementPdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class EmailBatchWriter {
    private final BatchService batchService;

    @Bean
    @StepScope
    public ItemWriter<EmailOutputData> emailWriter() {
        return chunk -> {
            List<Integer> batchIds = chunk.getItems().stream()
                    .filter(EmailOutputData::getResult)
                    .map(EmailOutputData::getBatchId)
                    .toList();
            batchService.updateEmailResultByIds(batchIds);
        };
    }
}
