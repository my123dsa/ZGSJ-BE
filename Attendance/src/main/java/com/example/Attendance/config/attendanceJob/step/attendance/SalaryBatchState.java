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

//sate는 step이 진행될때 특정상태가 필요해서 만들게된 객체
//그래서 scope를 이용해 job의 생명주기로 이객체를 생성하고 초기화함
//stepscope를 사용해야되는데 조금 꼬여서 그냥 jobscope를 사용했음
// 내 생각에는 이 state를 최소화 및 잘 사용해야  시간 최소화 할 수 있지 않을까?

@Component
@JobScope   // StepScope 대신 JobScope 사용
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)   // Job 스코프로 생성
@Getter
public class SalaryBatchState {

    private List<BatchInputData> employees;  // 이체일에 해당하는 직원들
    private int currentIndex = 0;
    private LocalDate localDate;
    private List<CommuteSummary> commutes; // 직원의 일한 시간들
    // 그직원들의 id리스트<- 지금 보니 굳이 필요없어보이네?
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