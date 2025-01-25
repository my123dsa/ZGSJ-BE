package com.example.Attendance.config.attendanceJob.step.email;

import com.example.Attendance.model.Batch;
import com.example.Attendance.dto.batch.email.EmailInputData;
import lombok.Getter;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;

//email 부분의 상태 값 모음
//이부분 관성적으로 만들었는데 뺄 수 있을 것 같은 느낌?

@Component
@JobScope   // StepScope 대신 JobScope 사용
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)   // Job 스코프로 생성
@Getter
public class EmailBatchState {

    private List<EmailInputData> batches;
    private int index=0;

    public void findAllByLocalDate(List<Batch> batches) {
        this.batches =batches.stream().map(EmailInputData::from).toList();
    }

    public void reset(){
        this.index=0;
        this.batches=null;
    }

    public EmailInputData findBatchInputData(){
        if (index<batches.size()) {
            return batches.get(index++);
        }
        return null;
    }
}
