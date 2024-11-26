package com.example.Attendance.config.attendanceJob.step.pdf;

import com.example.Attendance.model.Batch;
import com.example.Attendance.dto.batch.pdf.PdfInputData;
import lombok.Getter;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@JobScope   // StepScope 대신 JobScope 사용
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)  // Job 스코프로 생성
@Getter
public class PdfBatchState {

    private List<PdfInputData> batches;
    private int index=0;

    public void findAllByLocalDate(List<Batch> batches) {
        this.batches =batches.stream().map(PdfInputData::from).toList();
    }
    public void upIndex(){
        this.index++;
    }
    public void reset(){
        this.index=0;
        this.batches=null;
    }
}
