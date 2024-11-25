package com.example.Attendance.config.attendanceJob;

import com.example.Attendance.config.attendanceJob.step.attendance.AttendanceBatchProcessor;
import com.example.Attendance.config.attendanceJob.step.attendance.AttendanceBatchReader;
import com.example.Attendance.config.attendanceJob.step.attendance.AttendanceBatchWriter;
import com.example.Attendance.config.attendanceJob.step.email.EmailBatchProcessor;
import com.example.Attendance.config.attendanceJob.step.email.EmailBatchReader;
import com.example.Attendance.config.attendanceJob.step.email.EmailBatchWriter;
import com.example.Attendance.config.attendanceJob.step.pdf.PdfBatchProcessor;
import com.example.Attendance.config.attendanceJob.step.pdf.PdfBatchReader;
import com.example.Attendance.config.attendanceJob.step.pdf.PdfBatchWriter;
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

    private final AttendanceBatchJobListener attendanceBatchJobListener;

    private final AttendanceBatchProcessor attendanceBatchProcessor;
    private final AttendanceBatchWriter attendanceBatchWriter;
    private final AttendanceBatchReader attendanceBatchReader;

    private final EmailBatchProcessor emailBatchProcessor;
    private final EmailBatchReader emailBatchReader;
    private final EmailBatchWriter emailBatchWriter;

    private final PdfBatchProcessor pdfBatchProcessor;
    private final PdfBatchReader pdfBatchReader;
    private final PdfBatchWriter pdfBatchWriter;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;


    @Bean
    public Job attendanceJob() {
        return new JobBuilder("automaticTransferJob", jobRepository)
                .listener(attendanceBatchJobListener.attendanceJobListener()) // Listener 등록
                .start(attendanceStep())
                .next(statementPdfStep())
                .next(statementEmailStep())
                .build();
    }

    @Bean
    public Step attendanceStep() {
        return new StepBuilder("automaticTransferStep", jobRepository)
                .<BatchInputData, BatchOutputData>chunk(3, transactionManager)
                .reader(attendanceBatchReader.attendanceReader())       // 데이터 읽기
                .processor(attendanceBatchProcessor.attendanceProcessor()) // 데이터 처리
                .writer(attendanceBatchWriter.attendanceWriter())       // 데이터 쓰기
                .build();
    }

    @Bean
    public Step statementPdfStep() {
        return new StepBuilder("statementPdfStep", jobRepository)
                .<PdfInputData, PdfOutputData>chunk(3, transactionManager)
                .reader(pdfBatchReader.pdfReader())       // 데이터 읽기
                .processor(pdfBatchProcessor.pdfProcessor()) // 데이터 처리
                .writer(pdfBatchWriter.pdfWriter())       // 데이터 쓰기
                .build();
    }

    @Bean
    public Step statementEmailStep() {
        return new StepBuilder("statementEmailStep", jobRepository)
                .<EmailInputData, EmailOutputData>chunk(3, transactionManager)
                .reader(emailBatchReader.emailReader())       // 데이터 읽기
                .processor(emailBatchProcessor.emailProcessor()) // 데이터 처리
                .writer(emailBatchWriter.emailWriter())       // 데이터 쓰기
                .build();
    }
}
