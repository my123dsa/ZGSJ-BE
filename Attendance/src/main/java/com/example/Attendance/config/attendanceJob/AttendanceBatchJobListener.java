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

    // job이 끝났을 때 state값들을 리셋시켜 이후 배치에 영향을 안 가지게 해줌
    // 원래는 이거 없어도 되는 걸로 아는데, 실행할 때마다 초기화가 안되서 최종적으로 집어넣음
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
