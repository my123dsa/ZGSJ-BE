package com.example.Attendance.config.attendanceJob.step.attendance;

import com.example.Attendance.dto.batch.BatchInputData;
import com.example.Attendance.dto.batch.CommuteSummary;
import com.example.Attendance.service.CommuteService;
import com.example.Attendance.service.StoreEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class SalaryBatchReader implements ItemReader<BatchInputData> {

    private final StoreEmployeeService storeEmployeeService;
    private final CommuteService commuteService;

    private List<BatchInputData> employees;
    private List<CommuteSummary> commutes;
    private int index = 0;

    @BeforeStep
    public void init() {
        LocalDate localDate = LocalDate.now();
        int paymentDay = localDate.getDayOfMonth();

        employees = storeEmployeeService.findStoreEmployeeByTypeAndPaymentDate(paymentDay);
//        employees = storeEmployeeService.findStoreEmployeeByTypeAndPaymentDate(20);
        List<Integer> employeeIds = employees.stream().map(BatchInputData::getSeId).toList();
        commutes = commuteService.findAllByCommuteDateBetween(employeeIds, localDate);

//        stepExecution.getExecutionContext().put("commutes", commutes);
    }

    @Override
    public BatchInputData read() {
        if (employees != null && index < employees.size()) {
            return employees.get(index++);
        }
        return null;
    }

    public CommuteSummary getCommuteSummary(Integer seId) {
        return commutes.stream()
                .filter(c -> c.getEmployeeId().equals(seId))
                .map(CommuteSummary::updateDuration)
                .findFirst()
                .orElse(null);
    }
}
