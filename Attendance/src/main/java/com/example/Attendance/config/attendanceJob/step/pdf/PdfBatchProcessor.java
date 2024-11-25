package com.example.Attendance.config.attendanceJob.step.pdf;

import com.example.Attendance.dto.batch.pdf.PdfInputData;
import com.example.Attendance.dto.batch.pdf.PdfOutputData;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.service.GCPService;
import com.example.Attendance.service.PayStatementPdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PdfBatchProcessor {

    private final GCPService gCPService;
    private final PayStatementPdfService payStatementPdfService;

    @Bean
    @StepScope
    public ItemProcessor<PdfInputData, PdfOutputData> pdfProcessor() {
        return item -> {
            try {
                byte[] pdf = payStatementPdfService.generateIncomeStatementPdf(item);
                String url = gCPService.uploadObject(pdf);
                return PdfOutputData.of(item, url,true);
            } catch (Exception e) {
                throw new CustomException(ErrorCode.SERVER_ERROR);
            }
        };
    }
}
