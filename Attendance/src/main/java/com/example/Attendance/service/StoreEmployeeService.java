package com.example.Attendance.service;

import com.example.Attendance.dto.EmployeeNameResponse;
import com.example.Attendance.repository.StoreEmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreEmployeeService {

    private final StoreEmployeeRepository storeEmployeeRepository;

    @Transactional
    public List<EmployeeNameResponse> getEmployeeNamesByStore(int storeId)
    {
        return storeEmployeeRepository.findSimpleInfoByStoreId(storeId);
    }
}
