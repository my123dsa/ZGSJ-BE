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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class JobScheduler {

    private final JobLauncher jobLauncher;
    private final Job attendanceJob;

    @Scheduled(cron = "0 * * * * *")  // 매일 새벽 4시 실행
//@Scheduled(cron = "0 0 4 * * *")
    public void runJob() {


        String dateParam = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 여기는 배치라기 보다 스케쥴러이고 아래 내용들은 job에 날짜나 시간을 파라미터로 넣어서
        // job에 대한 메타데이터 즉 고유값을 넣어주는 느낌
        try {
            // date, time을 job 시작할 때 만들어서 며칠 몇시에 job 뭐 했다라고 알 수 있게하기 위한 메타데이터
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("date", dateParam)  // 날짜 파라미터 추가
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            log.info("급여 이체 Job 시작: {}", dateParam);
            //job실행
            JobExecution execution = jobLauncher.run(attendanceJob, jobParameters);

            log.info("Job 실행 완료 - Status: {}, Start: {}, End: {}",
                    execution.getStatus(),
                    execution.getStartTime(),
                    execution.getEndTime());

            if (execution.getStatus() == BatchStatus.FAILED) {
                log.error("Job 실행 실패 - Exit Description: {}",
                        execution.getExitStatus().getExitDescription());
            }

        //예외 처리

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