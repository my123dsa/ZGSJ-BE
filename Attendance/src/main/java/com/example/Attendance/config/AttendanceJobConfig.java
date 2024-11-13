package com.example.Attendance.config;

import com.example.Attendance.FeignWithCoreBank;
import com.example.Attendance.Repository.CommuteRepository;
import com.example.Attendance.Repository.PayStatementRepository;
import com.example.Attendance.Repository.StoreEmployeeRepository;
import com.example.Attendance.dto.*;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.model.PayStatement;
import com.example.Attendance.model.StoreEmployee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AttendanceJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final StoreEmployeeRepository storeEmployeeRepository;
    private final PayStatementRepository payStatementRepository;
    private final CommuteRepository commuteRepository;
    private final FeignWithCoreBank feignWithCoreBank;

    @Bean
    public Job attendanceJob() {
        return new JobBuilder("automaticTransferJob", jobRepository)
                .start(attendanceStep())
                .build();
    }

    @Bean
    public Step attendanceStep() {
        return new StepBuilder("automaticTransferStep", jobRepository)
                .<BatchInputData, BatchOutputData>chunk(100, transactionManager)
                .reader(attendanceReader())       // 데이터 읽기
                .processor(attendanceProcessor()) // 데이터 처리
                .writer(attendanceWriter())       // 데이터 쓰기
                .build();
    }

    @Bean
    public ItemReader<BatchInputData> attendanceReader() {
        return new ItemReader<BatchInputData>() {
            private List<StoreEmployee> employees;  // 조회한 데이터를 담아두는 리스트
            private int currentIndex = 0;           // 현재 위치를 추적하는 인덱스
            private LocalDate localDate = LocalDate.now();
            private List<CommuteSummary> commutes;

            @Override
            public BatchInputData read() {
                try {
                    // 최초 데이터 로드
//                    if (employees == null) {
//                        employees = storeEmployeeRepository
//                                .findAllByPaymentDate(localDate.getDayOfMonth());
//                        log.info("직원 데이터 {}건을 조회했습니다.", employees.size());
//
//                        if (employees.isEmpty()) {
//                            log.info("처리할 직원 데이터가 없습니다.");
//                            return null;
//                        }
//                    }
                    employees = storeEmployeeRepository
                            .findAllByPaymentDate(localDate.getDayOfMonth());

                    if (commutes == null) {
                        //이거 년도랑 월만 받아야한다  이거 -30 해야되나
                        commutes = commuteRepository.findAllByCommuteDate(localDate);
                        log.info("출퇴근 데이터 {}건을 조회했습니다.", commutes.size());
                    }

                    // 데이터 반환
                    if (currentIndex < employees.size()) {
                        StoreEmployee employee = employees.get(currentIndex);
                        Long commuteDuration = commutes.stream()
                                .filter(commuteSummary -> commuteSummary.getId().equals(employee.getId()))
                                .map(CommuteSummary::getCommuteDuration)
                                .findFirst()
                                .orElse(0L);

                        currentIndex++;
                        log.debug("Processing employee {} ({}/{})",
                                employee.getId(), currentIndex, employees.size());

                        return BatchInputData.of(employee, commuteDuration);
                    }

                    log.info("모든 직원 데이터 처리 완료");
                    return null;
                } catch (Exception e) {
                    log.error("데이터 읽기 실패: {}", e.getMessage(), e);
                    throw new CustomException(ErrorCode.API_SERVER_ERROR);
                }
            }
        };
    }

    @Bean
    public ItemProcessor<BatchInputData, BatchOutputData> attendanceProcessor() {
        return item -> {
            try {
                TransferRequest request = TransferRequest.from(item);

                // 외부 API 호출
                TransferResponse response = feignWithCoreBank.automaticTransfer(request);
                if(response.getStatus()==200){
                    return BatchOutputData.of(item.getSeId(), item.getAmount(), response);
                }
                return null;
            } catch (Exception e) {
                log.error("송금 처리 실패 - employee: {}, error: {}",
                        item.getSeId(), e.getMessage());
                return null;
            }
        };
    }

    @Bean
    public ItemWriter<BatchOutputData> attendanceWriter() {
        return chunk -> {
            List<PayStatement> payStatements = chunk.getItems().stream()
                    .map(BatchOutputData::toEntity)
                    .collect(Collectors.toList());

            payStatementRepository.saveAll(payStatements);
            log.info("급여 이체 결과 {} 건 저장 완료", chunk.size());
        };
    }
}