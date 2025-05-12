package com.example.Attendance.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class JobScheduler {

    private final JobLauncher jobLauncher;
    private final Job attendanceJob; //job이 하나라 자동으로 해당 job주입

//    @Scheduled(cron = "0 * * * * *")  // 매일 새벽 4시 실행
@Scheduled(cron = "0 0 4 * * *")
//    @Scheduled(cron = "0 */5 * * * *")
    public void runJob() {
        String dateParam = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("date", dateParam)  // 날짜 파라미터 추가
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            log.info("급여 이체 Job 시작: {}", dateParam);

            JobExecution execution = jobLauncher.run(attendanceJob, jobParameters);

            log.info("Job 실행 완료 - Status: {}, Start: {}, End: {}",
                    execution.getStatus(),
                    execution.getStartTime(),
                    execution.getEndTime());
            LocalDateTime start = execution.getStartTime();
            LocalDateTime end = execution.getEndTime();
            Duration duration = Duration.between(start, end);
            log.info("\n");

            for (StepExecution stepExecution : execution.getStepExecutions()) {
                log.info("Step 이름: {}", stepExecution.getStepName());
                log.info("  읽은 아이템 수 (readCount): {}", stepExecution.getReadCount());
                log.info("  처리된 아이템 수 (processSkipCount): {}", stepExecution.getProcessSkipCount());
                log.info("  쓰기된 아이템 수 (writeCount): {}", stepExecution.getWriteCount());
                log.info("  커밋 횟수 (commitCount): {}", stepExecution.getCommitCount());
                log.info("  롤백 횟수 (rollbackCount): {}", stepExecution.getRollbackCount());
            }
            log.info("총 실행 시간: {}ms)", duration.toMillis());

            if (execution.getStatus() == BatchStatus.FAILED) {
                log.error("Job 실행 실패 - Exit Description: {}",
                        execution.getExitStatus().getExitDescription());
            }

        } catch (JobExecutionAlreadyRunningException e) {
            log.error("Job이 이미 실행 중입니다", e);
        } catch (JobRestartException e) {
            log.error("Job을 재시작할 수 없습니다", e);
        } catch (JobInstanceAlreadyCompleteException e) {
            log.error("이미 완료된 Job입니다", e);
        } catch (JobParametersInvalidException e) {
            log.error("잘못된 Job 파라미터입니다", e);
        } catch (Exception e) {
            log.error("Job 실행 중 예외 발생: {}", e.getMessage(), e);
        }
    }
}