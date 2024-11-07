package com.example.User.controller;

import com.example.User.dto.storeemployee.StoreEmployeeRequest;
import com.example.User.dto.storeemployee.StoreEmployeeUpdateRequest;
import com.example.User.service.StoreEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@Slf4j
public class StoreEmployeeController {

    private final StoreEmployeeService storeemployeeService;

    @PostMapping
    public ResponseEntity<String> registerEmployee(
            @RequestBody StoreEmployeeRequest request,
            @RequestParam Integer storeId
    ) {
        storeemployeeService.register(request, storeId);
        return ResponseEntity.ok("직원 등록 성공");
    }

    @PutMapping
    public ResponseEntity<String> updateEmployee(
            @RequestParam Integer seId,
            @RequestBody StoreEmployeeUpdateRequest request
    ) {
        storeemployeeService.updateEmployee(seId, request);
        return ResponseEntity.ok("직원 수정 성공");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteEmployee(
            @RequestParam Integer seId
    ) {
        storeemployeeService.deleteEmployee(seId);
        return ResponseEntity.ok("직원 삭제 성공");
    }



}

