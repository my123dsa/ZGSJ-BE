package com.example.Attendance.config;

import com.example.Attendance.dto.BatchInputData;
import com.example.Attendance.dto.CommuteSummary;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@JobScope   // Job 스코프로 생성
@Getter

public class BatchJobState {
    private List<BatchInputData> employees;
    private int currentIndex = 0;
    private LocalDate localDate;
    private List<CommuteSummary> commutes;
    private List<Integer> employeeIds;

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
        this.currentIndex+=1;
    }

    public Long getCommuteDuration(Integer seId) {
        return this.getCommutes().stream()
                .filter(commuteSummary -> commuteSummary.getEmployeeId().equals(seId))
                .map(CommuteSummary::getCommuteDuration)
                .findFirst()
                .orElse(0L);
    }
    public void reset(){
        this.currentIndex = 0;
        this.employees = null;
        this.commutes = null;
        this.localDate = null;
        this.employeeIds= null;
    }
}