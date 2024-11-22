package com.example.Attendance.config.attendanceJob;

import com.example.Attendance.dto.batch.BatchOutputData;
import com.example.Attendance.model.PayStatement;
import com.example.Attendance.repository.PayStatementRepository;
import com.example.Attendance.service.EmailService;
import com.example.Attendance.service.GCPService;
import com.example.Attendance.service.PayStatementPdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchWriter {
    private final PayStatementRepository payStatementRepository;
    private final EmailService emailService;
    private final GCPService gCPService;
    private final PayStatementPdfService payStatementPdfService;

    @Bean
    public ItemWriter<BatchOutputData> attendanceWriter() {
        return chunk -> {
            List<PayStatement> payStatements = chunk.getItems().stream()
                    .map(item -> {
                        byte[] pdf = payStatementPdfService.generateIncomeStatementPdf(item);
                        String url = gCPService.uploadObject(pdf);
//                        emailService.sendPayStatement(item.getEmail(),url);
                        return item.toEntity(url);
                    })
                    .collect(Collectors.toList());

            payStatementRepository.saveAll(payStatements);
            log.info("급여 이체 결과 {} 건 저장 완료", chunk.size());
        };
    }

}
