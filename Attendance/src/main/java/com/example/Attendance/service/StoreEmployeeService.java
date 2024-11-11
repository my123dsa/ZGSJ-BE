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

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreEmployeeService {
    private final StoreEmployeeRepository storeEmployeeRepository;
    private final CommuteRepository commuteRepository;


    public void goToWork(Integer storeId,EmployeeCommuteRequest request){
        StoreEmployee storeEmployee =findStoreEmployeeByPhoneNumberWithStore(storeId,request);

        Commute lastCommute= commuteRepository.findTopById(storeEmployee.getId()).get();
        LocalDateTime now= LocalDateTime.now();
        Commute commute= Commute.createCommuteCheckIn(now.toLocalDate(),now,storeEmployee);

        if (lastCommute!=null && lastCommute.getEndTime()==null){
            commuteRepository.save(commute);
            throw new CustomException(ErrorCode.MISSING_LEAVE_WORK_RECODE);
        }
        commuteRepository.save(commute);
    }

    public void leaveWork(Integer storeId,EmployeeCommuteRequest request){
        StoreEmployee storeEmployee =findStoreEmployeeByPhoneNumberWithStore(storeId,request);

        Commute commute= commuteRepository.findTopById(storeEmployee.getId())
                .orElseThrow(()-> new CustomException(ErrorCode.INVALID_COMMUTE));
        if(commute.getEndTime()!=null){
            throw new CustomException(ErrorCode.MISSING_GO_TO_WORK_RECODE);
        }
        commute.setEndTime(LocalDateTime.now());
    }

    public StoreEmployee findStoreEmployeeByPhoneNumberWithStore(Integer storeId,EmployeeCommuteRequest request) {
        StoreEmployee storeEmployee=storeEmployeeRepository.findByEmailAndStoreId(request.getEmail(),storeId)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_EMPLOYEE));

        if (storeEmployee.getStore().getLocation().equals(request.getLocation())){
            throw new CustomException(ErrorCode.INVALID_LOCATION);
        }
        return storeEmployee;

    }
}
