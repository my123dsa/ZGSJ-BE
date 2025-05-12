package com.example.Attendance.config.attendanceJob.step.pdf;

import com.example.Attendance.dto.batch.pdf.PdfInputData;
import com.example.Attendance.model.Batch;
import com.example.Attendance.service.batch.BatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class PdfBatchReader implements ItemReader<PdfInputData> {

    private final BatchService batchService;

    private Iterator<PdfInputData> iterator;
    @BeforeStep
    public void beforeStep() {
        List<Batch> batchList = batchService.findAllByLocalDateWithBankResultIsTrue(LocalDate.now());

        List<PdfInputData> pdfList = batchList.stream()
                .map(PdfInputData::from)
                .toList(); // ✅ Stream 종료

        this.iterator = pdfList.iterator(); //
//        stepExecution.getExecutionContext().put("pdfBatchSize", batchList.size());
    }

    @Override
    public PdfInputData read() {
        if (iterator != null && iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }
}