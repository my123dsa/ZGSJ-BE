package com.example.Attendance.config.attendanceJob;

import com.example.Attendance.dto.batch.BatchOutputData;
import com.example.Attendance.model.PayStatement;
import com.example.Attendance.repository.PayStatementRepository;
import com.example.Attendance.service.EmailService;
import com.example.Attendance.service.GCPService;
import com.example.Attendance.service.PayStatementPdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchWriter {
    private final PayStatementRepository payStatementRepository;
    private final EmailService emailService;
    private final GCPService gCPService;
    private final PayStatementPdfService payStatementPdfService;

    @Bean
    public ItemWriter<BatchOutputData> attendanceWriter() {
        return chunk -> {
            List<PayStatement> payStatements = chunk.getItems().stream()
                    .map(item -> {
                        /// state는 너무 위험하다
                        // 점검시간 외  시간에서 발생하는 오류는
                        // 출퇴근은 너무 모인다.   점심시간이 오히려 괜찮다?
                        // 실패시 재시도 하지말고 실패 메일을 보내자!  그리고 이에 대한 실패를 재시도하는건 오바다.
                        byte[] pdf = payStatementPdfService.generateIncomeStatementPdf(item);
                        String url = gCPService.uploadObject(pdf);
                        /// 테이블 하나?
                        //이메일 나누고!!!
//                        emailService.sendPayStatement(item.getEmail(),url);
                        return item.toEntity(url);
                    })
                    .collect(Collectors.toList());

            payStatementRepository.saveAll(payStatements);
            log.info("급여 이체 결과 {} 건 저장 완료", chunk.size());
        };
    }

}
