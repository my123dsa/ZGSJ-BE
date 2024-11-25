package com.example.Attendance.config.attendanceJob;

import com.example.Attendance.config.attendanceJob.step.email.EmailBatchState;
import com.example.Attendance.config.attendanceJob.step.pdf.PdfBatchState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class AttendanceBatchJobListener {

    private final PdfBatchState pdfBatchState;
    private final EmailBatchState emailBatchState;
    private final BatchJobState batchJobState;

    @Bean
    public JobExecutionListener attendanceJobListener() {
        return new JobExecutionListener() {
            @Override
            public void afterJob(JobExecution jobExecution) {
                // 컨텍스트에 다시 저장
                pdfBatchState.reset();
                emailBatchState.reset();
                batchJobState.reset();
//                batchJobState.getFailedEmployee().stream().map();
                // 사장에게 메일보내기 한번에
            }
        };
    }
}
