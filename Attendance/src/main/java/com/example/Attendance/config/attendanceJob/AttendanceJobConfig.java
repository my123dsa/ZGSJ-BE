package com.example.Attendance.config.attendanceJob;

import com.example.Attendance.config.attendanceJob.step.attendance.SalaryBatchStep;
import com.example.Attendance.config.attendanceJob.step.email.EmailBatchStep;
import com.example.Attendance.config.attendanceJob.step.pdf.PdfBatchStep;
import com.example.Attendance.dto.batch.BatchInputData;
import com.example.Attendance.dto.batch.BatchOutputData;
import com.example.Attendance.dto.batch.email.EmailInputData;
import com.example.Attendance.dto.batch.email.EmailOutputData;
import com.example.Attendance.dto.batch.pdf.PdfInputData;
import com.example.Attendance.dto.batch.pdf.PdfOutputData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AttendanceJobConfig {

    private final SalaryBatchStep salaryBatchStep;
    private final EmailBatchStep emailBatchStep;
    private final PdfBatchStep pdfBatchStep;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final AttendanceBatchJobListener attendanceBatchJobListener;

    // 먼저 1개의 job과 3개의 스텝을 작성
    // 사실 3개의 job을 해야하는데 시간이 부족해 교체하지 못했음... <- 수정한다면 요기부터 일듯

    @Bean
    public Job attendanceJob() {
        // job에 해당하는  listener와 step을 작성해주는데 각 스텝의 경우는 1개의 스텝이 오류로 종료되어도 다음이 진행되어야하기 때문에
        // start, on, to, from등의 메소드를 사용함
        return new JobBuilder("automaticTransferJob", jobRepository)
                .listener(attendanceBatchJobListener.attendanceJobListener()) // Listener 등록
                .start(attendanceStep()).on("*").to(statementPdfStep())
                .from(statementPdfStep()).on("*").to(statementEmailStep())
                .from(statementEmailStep()).on("*").end()
                .end()
                .build();
    }

    // 기본적으로 스텝은 읽기, 처리, 쓰기가 존재
    // 읽기에서 대량으로 처리한 후-> 처리에서는 1개씩 -> 쓰기에서는 처리가 n(chunkSize)개 완료시 한번에 db에 적용함
    @Bean
    public Step attendanceStep() {
        return new StepBuilder("automaticTransferStep", jobRepository)
                .<BatchInputData, BatchOutputData>chunk(3, transactionManager)
                .reader(salaryBatchStep.salaryReader())       // 데이터 읽기
                .processor(salaryBatchStep.salaryProcessor()) // 데이터 처리
                .writer(salaryBatchStep.salaryWriter())       // 데이터 쓰기
                .build();
    }

    @Bean
    public Step statementPdfStep() {
        return new StepBuilder("statementPdfStep", jobRepository)
                .<PdfInputData, PdfOutputData>chunk(3, transactionManager)
                .reader(pdfBatchStep.pdfReader())       // 데이터 읽기
                .processor(pdfBatchStep.pdfProcessor()) // 데이터 처리
                .writer(pdfBatchStep.pdfWriter())       // 데이터 쓰기
                .build();
    }

    @Bean
    public Step statementEmailStep() {
        return new StepBuilder("statementEmailStep", jobRepository)
                .<EmailInputData, EmailOutputData>chunk(3, transactionManager)
                .reader(emailBatchStep.emailReader())       // 데이터 읽기
                .processor(emailBatchStep.emailProcessor()) // 데이터 처리
                .writer(emailBatchStep.emailWriter())       // 데이터 쓰기
                .build();
    }
}
