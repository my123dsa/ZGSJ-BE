package com.example.Attendance.config.attendanceJob;

import com.example.Attendance.dto.batch.BatchInputData;
import com.example.Attendance.dto.batch.CommuteSummary;
import com.example.Attendance.error.ErrorDTO;
import lombok.Getter;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@JobScope   // StepScope 대신 JobScope 사용
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)   // Job 스코프로 생성
@Getter
public class BatchJobState {

    private List<BatchInputData> employees;
    private int currentIndex = 0;
    private LocalDate localDate;
    private List<CommuteSummary> commutes;
    private List<Integer> employeeIds;
    private final List<BatchInputData> failedEmployee = new ArrayList<>();
    //여기 사장 이메일과 직원 이름 계좌 ?    정도만 넣어 메일 보내자


    public void setCommutes(List<CommuteSummary> commutes) {
        this.commutes = commutes;
    }

    public void setEmployees(List<BatchInputData> employees) {
        this.employees = employees;
        this.employeeIds = employees.stream()
                .map(BatchInputData::getSeId)
                .toList();
    }

    public void setLocalDate() {
        this.localDate = LocalDate.now();
    }

    public void indexUp() {
        this.currentIndex += 1;
    }

    public CommuteSummary getCommuteDuration(Integer seId) {
        return commutes.stream()
                .filter(cs -> cs.getEmployeeId().equals(seId))
                .map(CommuteSummary::updateDuration)
                .findFirst()
                .orElse(null);
    }

    public void reset() {
        this.currentIndex = 0;
        this.employees = null;
        this.commutes = null;
        this.localDate = null;
        this.employeeIds = null;
//        this.failedEmployee.clear();
    }

    private void handleInsufficientBalance(BatchInputData item, ErrorDTO dto) {
        failedEmployee.add(item);
    }
}