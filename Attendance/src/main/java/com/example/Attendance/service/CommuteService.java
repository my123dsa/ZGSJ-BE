package com.example.Attendance.service;

import com.example.Attendance.Repository.CommuteRepository;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.model.Commute;
import com.example.Attendance.model.StoreEmployee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommuteService {
    private final CommuteRepository commuteRepository;

    @Transactional
    public boolean goToWork(StoreEmployee storeEmployee){
        Optional<Commute> lastCommute= commuteRepository.findTopByStoreEmployeeIdOrderByStartTimeDesc(storeEmployee.getId());
        LocalDateTime now= LocalDateTime.now();
        Commute commute= Commute.createCommuteCheckIn(now.toLocalDate(),now,storeEmployee);
        if(lastCommute.isPresent() &&lastCommute.get().getEndTime()==null){
            commuteRepository.save(commute);
            return false;
        }
        commuteRepository.save(commute);
        return true;
    }

    @Transactional
    public void leaveWork(StoreEmployee storeEmployee){
        Commute commute= commuteRepository
                .findTopByStoreEmployeeIdOrderByStartTimeDesc(storeEmployee.getId())
                .orElseThrow(()-> new CustomException(ErrorCode.INVALID_COMMUTE));
        if(commute.getEndTime()!=null){
            throw new CustomException(ErrorCode.MISSING_GO_TO_WORK_RECODE);
        }
        commute.setEndTime(LocalDateTime.now());
    }
}
