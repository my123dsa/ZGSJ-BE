package com.example.Attendance.config;

import com.example.Attendance.service.batch.AttendanceConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

// 배치를 위한 기본 config
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;

    //배치를 사용하기 위해서는 데이터에 대한 정보인 메타데이터가 필요함
    // → 이를 저장해주기 위해 메타데이터테이블을 만드는 것
    // → 이에 따라 db에 접근해주게 하고 테이블 생성할 수 있게 만들어주는 코드
    // 근데 이걸 yaml로 빼줄 수 있음(나는 자동으로 테이블이 안 생겨서 넣어봤는데 그래도 자동으로는 안되더라)
    @Bean
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE");
        factory.setTablePrefix("BATCH_");
        factory.setMaxVarCharLength(1000);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    //얘는 배치중 처리 단위인 job을 실행시킬 수 있게 해주는 코드
    // jobRepository도 기존에 우리가 알던 객체Repository와 유사함 -> 위에서 만든 테이블에 메타데이터를 저장함
    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher launcher = new TaskExecutorJobLauncher();
        launcher.setJobRepository(jobRepository);
        launcher.afterPropertiesSet();
        return launcher;
    }

    //세금 계산 상수를 그냥 bean에 등록해 놓은 것 무시해도 좋음
    @Bean
    public AttendanceConstants attendanceConstants() {
        return new AttendanceConstants();
    }
}