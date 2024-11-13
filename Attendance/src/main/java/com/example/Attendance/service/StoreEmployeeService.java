package com.example.Attendance.service;

import com.example.Attendance.Repository.StoreEmployeeRepository;
import com.example.Attendance.dto.EmployeeCommuteRequest;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.model.StoreEmployee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreEmployeeService {
    private final StoreEmployeeRepository storeEmployeeRepository;

    public StoreEmployee findStoreEmployeeByEmailAndStoreId(Integer storeId,EmployeeCommuteRequest request) {
        StoreEmployee storeEmployee=storeEmployeeRepository.findByEmailAndStoreId(request.getEmail(),storeId)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_EMPLOYEE));
        Double sLat=storeEmployee.getStore().getLatitude();
        Double sLong=storeEmployee.getStore().getLongitude();
        double rangeLat = 0.0009; // 위도 100m 범위
        double rangeLon = 0.0011; // 경도 100m 범위

        Double reLat= request.getLatitude();
        Double reLong= request.getLongitude();
        boolean isLat=reLat <= sLat+rangeLat && reLat>=sLat-rangeLat;
        boolean isLong= reLong<= sLong+rangeLon && reLong>=sLong-rangeLon;
        if (!isLat || !isLong){
            throw new CustomException(ErrorCode.INVALID_LOCATION);
        }
        return storeEmployee;
    }
}
