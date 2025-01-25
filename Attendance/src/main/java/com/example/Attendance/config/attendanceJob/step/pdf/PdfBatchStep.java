package com.example.Attendance.config.attendanceJob.step.pdf;

import com.example.Attendance.service.PayStatementService;
import com.example.Attendance.service.batch.BatchService;
import com.example.Attendance.dto.batch.pdf.PdfInputData;
import com.example.Attendance.dto.batch.pdf.PdfOutputData;
import com.example.Attendance.dto.batch.pdf.PdfSaveData;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.model.PayStatement;
import com.example.Attendance.repository.PayStatementRepository;
import com.example.Attendance.service.batch.GCPService;
import com.example.Attendance.service.batch.PayStatementPdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor

public class PdfBatchStep {

    private final BatchService batchService;
    private final PdfBatchState pdfBatchState;
    private final PayStatementService payStatementService;
    private final GCPService gCPService;
    private final PayStatementPdfService payStatementPdfService;


    @Bean("pdfReader")
    public ItemReader<PdfInputData> pdfReader() {

        return () -> {
            try {
                if (pdfBatchState.getBatches()==null ) {
                    pdfBatchState.findAllByLocalDate(
                            batchService.findAllByLocalDateWithBankResultIsTrue(LocalDate.now()));
                }

                return pdfBatchState.findBatchInputData();
            } catch (Exception e) {
                log.error("데이터 읽기 실패: {}", e.getMessage(), e);
                throw new CustomException(ErrorCode.API_SERVER_ERROR);
            }
        };
    }

    @Bean("pdfProcessor")
    public ItemProcessor<PdfInputData, PdfOutputData> pdfProcessor() {
        return item -> {
            try {
                //각 data를 바탕으로 pdf생성
                byte[] pdf = payStatementPdfService.generateIncomeStatementPdf(item);
                //gcp에 업로드
                String url = gCPService.uploadObject(pdf);
                return PdfOutputData.of(item, url,true);
            } catch (Exception e) {
                throw new CustomException(ErrorCode.SERVER_ERROR);
            }
        };
    }

    @Bean("pdfWriter")
    public ItemWriter<PdfOutputData> pdfWriter() {
        return chunk -> {
            List<PayStatement> payStatements = chunk.getItems().stream()
                    .map(PdfOutputData::toEntity).toList();

            List<PdfSaveData> dataList= chunk.getItems().stream()
                    .map(PdfOutputData::toPdfSaveData).toList();

            batchService.updatePdfResultsAndUrls(dataList);
            payStatementService.saveAll(payStatements);
            log.info("급여 이체 결과 {} 건 저장 완료", chunk.size());
        };
    }
}
