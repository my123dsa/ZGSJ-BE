package com.example.Attendance.config.attendanceJob.step.email;


import com.example.Attendance.dto.batch.email.EmailInputData;
import com.example.Attendance.model.Batch;
import com.example.Attendance.service.batch.BatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class EmailBatchReader implements ItemReader<EmailInputData> {

    private final BatchService batchService;

    private Iterator<EmailInputData> iterator;

    @BeforeStep
    public void beforeStep() {
        List<Batch> batchList = batchService.findAllByLocalDate(java.time.LocalDate.now());

        // ✅ Stream → List → Iterator
        List<EmailInputData> emailList = batchList.stream()
                .map(EmailInputData::from)
                .toList();

        this.iterator = emailList.iterator();
//        stepExecution.getExecutionContext().put("emailBatchesSize", batchList.size());
    }

    @Override
    public EmailInputData read() {
        if (iterator != null && iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }
}
