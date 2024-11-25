package com.example.Attendance.config.attendanceJob.step.attendance;

import com.example.Attendance.config.attendanceJob.BatchJobState;
import com.example.Attendance.dto.batch.BatchInputData;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.repository.StoreEmployeeRepository;
import com.example.Attendance.service.CommuteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor

public class AttendanceBatchReader {

    private final BatchJobState batchJobState;
    private final StoreEmployeeRepository storeEmployeeRepository;
    private final CommuteService commuteService;

    @Bean
    public ItemReader<BatchInputData> attendanceReader() {
        return () -> {
            try {
                if (batchJobState.getEmployees() == null) {
                    batchJobState.setLocalDate();
                    batchJobState.setEmployees(storeEmployeeRepository
//                            .findAllBatchInputDataByPaymentDate(20)); //테스트용
                            .findAllBatchInputDataByPaymentDate(batchJobState.getLocalDate().getDayOfMonth()));
                    if (batchJobState.getEmployees().isEmpty()) {
                        log.info("처리할 직원 데이터가 없습니다.");
                        return null;
                    }
                    batchJobState.setCommutes(commuteService.
                            findAllByCommuteDateBetween(batchJobState.getEmployeeIds(), batchJobState.getLocalDate()));
                }

                // 데이터 반환
                if (batchJobState.getCurrentIndex() < batchJobState.getEmployees().size()) {
                    BatchInputData bid = batchJobState.getEmployees()
                            .get(batchJobState.getCurrentIndex());

                    batchJobState.indexUp();
                    return bid;
                }

                log.info("모든 직원 데이터 처리 완료");
                return null;
            } catch (Exception e) {
                log.error("데이터 읽기 실패: {}", e.getMessage(), e);
                throw new CustomException(ErrorCode.API_SERVER_ERROR);
            }
        };
    }
}
