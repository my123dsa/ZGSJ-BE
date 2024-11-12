package com.example.Attendance.service;

import com.example.Attendance.Repository.CommuteRepository;
import com.example.Attendance.Repository.StoreEmployeeRepository;
import com.example.Attendance.dto.EmployeeCommuteRequest;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.model.Commute;
import com.example.Attendance.model.StoreEmployee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreEmployeeService {
    private final StoreEmployeeRepository storeEmployeeRepository;
    private final CommuteRepository commuteRepository;

    @Transactional
    public boolean goToWork(Integer storeId,EmployeeCommuteRequest request){
        StoreEmployee storeEmployee =findStoreEmployeeByEmailAndStoreId(storeId,request);

        Commute lastCommute= commuteRepository.findTopByStoreEmployeeIdOrderByStartTimeDesc(storeEmployee.getId()).get();
        LocalDateTime now= LocalDateTime.now();
        Commute commute= Commute.createCommuteCheckIn(now.toLocalDate(),now,storeEmployee);

        if (lastCommute!=null && lastCommute.getEndTime()==null){
            commuteRepository.save(commute);
            return false;
        }
        commuteRepository.save(commute);
        return true;
    }

    @Transactional
    public void leaveWork(Integer storeId,EmployeeCommuteRequest request){
        StoreEmployee storeEmployee =findStoreEmployeeByEmailAndStoreId(storeId,request);

        log.info("working");
        Commute commute= commuteRepository.findTopByStoreEmployeeIdOrderByStartTimeDesc
                        (storeEmployee.getId())
                .orElseThrow(()-> new CustomException(ErrorCode.INVALID_COMMUTE));

        if(commute.getEndTime()!=null){
            throw new CustomException(ErrorCode.MISSING_GO_TO_WORK_RECODE);
        }

        commute.setEndTime(LocalDateTime.now());
    }

    public StoreEmployee findStoreEmployeeByEmailAndStoreId(Integer storeId,EmployeeCommuteRequest request) {
        StoreEmployee storeEmployee=storeEmployeeRepository.findByEmailAndStoreId(request.getEmail(),storeId)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_EMPLOYEE));
        Double sLat=storeEmployee.getStore().getLatitude();
        Double sLong=storeEmployee.getStore().getLongitude();
        double rangeLat = 0.0009; // 위도 100m 범위
        double rangeLon = 0.0011; // 경도 100m 범위

        Double reLat= request.getLatitude();
        Double reLong= request.getLongitude();
        boolean isLat=reLat <= sLat+rangeLat || reLat>=sLat-rangeLat;
        boolean isLong= reLong<= sLong+rangeLon || reLong>=sLong-rangeLon;
        if (!isLat || !isLong){
            throw new CustomException(ErrorCode.INVALID_LOCATION);
        }
        return storeEmployee;
    }
}
