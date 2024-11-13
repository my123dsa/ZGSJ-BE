package com.example.Attendance.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling  // 스케줄링 기능 활성화
@RequiredArgsConstructor
public class JobScheduler {

    private final JobLauncher jobLauncher;
    private final Job attendanceJob;  // AttendanceJobConfig에서 정의한 Job

    @Scheduled(cron = "0 0 4 * * *")  // 매일 새벽 4시 실행
    public void runJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(attendanceJob, jobParameters);
            log.info("Job Status : {}", execution.getStatus());
        } catch (Exception e) {
            log.error("Job Failed : {}", e.getMessage());
        }
    }
}