package com.example.Attendance.config.attendanceJob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchJobListener {

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
