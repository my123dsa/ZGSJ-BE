package com.example.Attendance.config.attendanceJob;

import com.example.Attendance.dto.batch.pdf.PdfSaveData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BatchService {
    private final BatchRepository batchRepository;

    public List<Batch> findAllByLocalDate(LocalDate date) {
        return batchRepository.findAllByIssuanceDate(date);
    }

    @Transactional
    public void updateEmailResultByIds(List<Integer> batchIds){
        batchRepository.updateEmailResultByIds(batchIds);
    }

    @Transactional
    public void updatePdfResultsAndUrls(List<PdfSaveData> dataList){
        for (PdfSaveData data : dataList) {
            batchRepository.updatePdfResultAndUrl(data.getId(), data.getUrl());
        }
    }
}
