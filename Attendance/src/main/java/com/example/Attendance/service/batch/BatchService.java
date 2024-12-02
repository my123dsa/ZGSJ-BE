package com.example.Attendance.service.batch;

import com.example.Attendance.dto.batch.pdf.PdfSaveData;
import com.example.Attendance.model.Batch;
import com.example.Attendance.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchService {
    private final BatchRepository batchRepository;

    public List<Batch> findAllByLocalDate(LocalDate date) {
        return batchRepository.findAllByIssuanceDate(date);
    }

    public List<Batch> findAllByLocalDateWithBankResultIsTrue(LocalDate date) {
        return batchRepository.findAllByLocalDateWithBankResultIsTrue(date);
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

    @Transactional
    public void saveAll(List<Batch> batches){
        batchRepository.saveAll(batches);
    }
}
