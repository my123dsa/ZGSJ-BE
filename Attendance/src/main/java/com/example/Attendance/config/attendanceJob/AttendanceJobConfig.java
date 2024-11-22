package com.example.Attendance.config.attendanceJob;

import com.example.Attendance.dto.BatchInputData;
import com.example.Attendance.dto.BatchOutputData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AttendanceJobConfig {

    private final BatchProcessor batchProcessor;
    private final BatchWriter batchWriter;
    private final BatchReader batchReader;
    private final BatchJobListener batchJobListener;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;


    @Bean
    public Job attendanceJob() {
        return new JobBuilder("automaticTransferJob", jobRepository)
                .listener(attendanceJobListener()) // Listener 등록
                .start(attendanceStep())
                .build();
    }

    @Bean
    public Step attendanceStep() {
        return new StepBuilder("automaticTransferStep", jobRepository)
                .<BatchInputData, BatchOutputData>chunk(3, transactionManager)
                .reader(batchReader.attendanceReader())       // 데이터 읽기
                .processor(batchProcessor.attendanceProcessor()) // 데이터 처리
                .writer(batchWriter.attendanceWriter())       // 데이터 쓰기
                .build();
    }

    @Bean
    public JobExecutionListener attendanceJobListener() {
        return new JobExecutionListener() {
            @Override
            public void afterJob(JobExecution jobExecution) {
                // 컨텍스트에 다시 저장
//                batchJobState.getFailedEmployee().stream().map();
                // 사장에게 메일보내기 한번에
            }
        };
    }
}
