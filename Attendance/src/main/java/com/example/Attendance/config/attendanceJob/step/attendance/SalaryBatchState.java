package com.example.Attendance.config.attendanceJob.step.attendance;

import com.example.Attendance.dto.batch.BatchInputData;
import com.example.Attendance.dto.batch.CommuteSummary;
import lombok.Getter;
import org.springframework.batch.core.configuration.annotation.JobScope;
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
public class SalaryBatchState {

    private List<BatchInputData> employees;
    private int currentIndex = 0;
    private LocalDate localDate;
    private List<CommuteSummary> commutes;
    private List<Integer> employeeIds;

    public void setCommutes(List<CommuteSummary> commutes) {
        this.commutes = commutes;
    }

    public boolean setEmployees(List<BatchInputData> employees) {
        if (employees.isEmpty()) {
            return false;
        }
        this.employees = employees;
        this.employeeIds = employees.stream()
                .map(BatchInputData::getSeId)
                .toList();
        return true;
    }

    public int getPaymentDay(){
        this.localDate= LocalDate.now();
        return localDate.getDayOfMonth();
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
    }

    public BatchInputData findBatchInputData(){
        if (this.currentIndex < employees.size()) {
            BatchInputData bid = employees.get(this.currentIndex);
            currentIndex++;
            return bid;
        }
        return null;
    }
}