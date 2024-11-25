package com.example.Attendance.config.attendanceJob.step.attendance;

import com.example.Attendance.config.attendanceJob.Batch;
import com.example.Attendance.config.attendanceJob.BatchRepository;
import com.example.Attendance.dto.batch.BatchOutputData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AttendanceBatchWriter {

    private final BatchRepository batchRepository;

    @StepScope
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ItemWriter<BatchOutputData> attendanceWriter() {
        return chunk -> {
            List<Batch> batches= chunk.getItems().stream().map(BatchOutputData::ToBatchEntity).toList();
            batchRepository.saveAll(batches);
            log.info("급여 이체 결과 {} 건 저장 완료", chunk.size());
        };
    }
}

//            List<PayStatement> payStatements = chunk.getItems().stream()
//                    .map(item -> {
//                        /// state는 너무 위험하다
//                        // 점검시간 외  시간에서 발생하는 오류는
//                        // 출퇴근은 너무 모인다.   점심시간이 오히려 괜찮다?
//                        // 실패시 재시도 하지말고 실패 메일을 보내자!  그리고 이에 대한 실패를 재시도하는건 오바다.
//                        byte[] pdf = payStatementPdfService.generateIncomeStatementPdf(item);
//                        String url = gCPService.uploadObject(pdf);
//                        /// 테이블 하나?
//                        //이메일 나누고!!!
////                        emailService.sendPayStatement(item.getEmail(),url);
//                        return item.toEntity(url);
//                    })
//                    .collect(Collectors.toList());
//
//            payStatementRepository.saveAll(payStatements);