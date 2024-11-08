package com.example.Attendance.controller;

import com.example.Attendance.dto.EmployeeNameResponse;
import com.example.Attendance.service.StoreEmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class StoreEmployeeController {

    private final StoreEmployeeService storeEmployeeService;

    @GetMapping("/names")
    public ResponseEntity<List<EmployeeNameResponse>> getEmployeeNamesByStore(@RequestParam int storeid)
    {
        return ResponseEntity.ok(storeEmployeeService.getEmployeeNamesByStore(storeid));
    }
}
