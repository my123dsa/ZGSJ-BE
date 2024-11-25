package com.example.Attendance.config.attendanceJob.step.pdf;

import com.example.Attendance.config.attendanceJob.BatchService;
import com.example.Attendance.dto.batch.pdf.PdfInputData;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Slf4j
@Configuration
@RequiredArgsConstructor

public class PdfBatchReader {

    private final BatchService batchService;
    private final PdfBatchState pdfBatchState;


    @Bean
    @StepScope
    public ItemReader<PdfInputData> pdfReader() {
        pdfBatchState.findAllByLocalDate(batchService.findAllByLocalDate(LocalDate.now()));
        return () -> {
            try {
                if (pdfBatchState.getIndex()<pdfBatchState.getBatches().size()) {
                    PdfInputData inputData= pdfBatchState.getBatches().get(pdfBatchState.getIndex());
                    pdfBatchState.upIndex();
                    return inputData;
                }
                return null;
            } catch (Exception e) {
                log.error("데이터 읽기 실패: {}", e.getMessage(), e);
                throw new CustomException(ErrorCode.API_SERVER_ERROR);
            }
        };
    }
}
