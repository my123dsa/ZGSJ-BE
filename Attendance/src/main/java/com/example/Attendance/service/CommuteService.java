package com.example.Attendance.service;

import com.example.Attendance.dto.CommuteByPresidentRequest;
import com.example.Attendance.dto.CommuteMonthlyResponse;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.model.Commute;
import com.example.Attendance.model.StoreEmployee;
import com.example.Attendance.repository.CommuteRepository;
import com.example.Attendance.repository.StoreEmployeeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class CommuteService {

    private final CommuteRepository commuteRepository;
    private final StoreEmployeeRepository storeEmployeeRepository;

    @Transactional
    public void addDailyCommuteByPresident(@Valid CommuteByPresidentRequest request, int seId) {
        StoreEmployee employee = storeEmployeeRepository.findById(seId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Commute commute = request.toEntity(employee);
        commuteRepository.save(commute);
    }

    @Transactional
    public void updateDailyCommuteByPresident(@Valid CommuteByPresidentRequest request, int commuteId) {

        long commuteDuration = request.getEndTime() == null ?
                0L :
                calculateDuration(request.getStartTime(), request.getEndTime());

        commuteRepository.updateCommute(request.getStartTime(), request.getEndTime(),
                request.getCommuteDate(), commuteDuration, commuteId);
    }

    private long calculateDuration(LocalDateTime startTime, LocalDateTime endTime) {

        return  ChronoUnit.MINUTES.between(startTime, endTime);
    }

    @Transactional
    public void deleteDailyCommuteByPresident(int commuteid) {
        commuteRepository.deleteById(commuteid);
    }

    public List<CommuteMonthlyResponse> getMonthlyCommuteList(int storeId, int year, int month) {
        return commuteRepository.findMonthlyCommutesByStore(storeId,year,month)
                .stream().map(CommuteMonthlyResponse::from).toList();
    }
}
