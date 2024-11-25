package com.example.Attendance.config.attendanceJob.step.pdf;

import com.example.Attendance.config.attendanceJob.BatchService;
import com.example.Attendance.dto.batch.pdf.PdfOutputData;
import com.example.Attendance.dto.batch.pdf.PdfSaveData;
import com.example.Attendance.model.PayStatement;
import com.example.Attendance.repository.PayStatementRepository;
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
public class PdfBatchWriter {
    private final PayStatementRepository payStatementRepository;
    private final BatchService batchService;

    @Bean
    @StepScope
    public ItemWriter<PdfOutputData> pdfWriter() {
        return chunk -> {
            List<PayStatement> payStatements = chunk.getItems().stream()
                    .map(PdfOutputData::toEntity).toList();

            List<PdfSaveData> dataList= chunk.getItems().stream()
                    .map(PdfOutputData::toPdfSaveData).toList();

            batchService.updatePdfResultsAndUrls(dataList);
            payStatementRepository.saveAll(payStatements);
            log.info("급여 이체 결과 {} 건 저장 완료", chunk.size());
        };
    }
}
