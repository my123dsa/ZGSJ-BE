package com.example.Attendance.config.attendanceJob;

import com.example.Attendance.config.attendanceJob.step.attendance.SalaryBatchState;
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
    private final SalaryBatchState salaryBatchState;

    @Bean
    public JobExecutionListener attendanceJobListener() {
        return new JobExecutionListener() {
            @Override
            public void afterJob(JobExecution jobExecution) {
                pdfBatchState.reset();
                emailBatchState.reset();
                salaryBatchState.reset();
            }
        };
    }
}
