package com.example.Attendance.controller;


import com.example.Attendance.Repository.StoreEmployeeRepository;
import com.example.Attendance.dto.EmployeeCommuteRequest;
import com.example.Attendance.service.MapService;
import com.example.Attendance.service.StoreEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StoreEmployeeController {

    private final MapService mapService;
    private final StoreEmployeeService storeEmployeeService;

    @PostMapping("/go-to-work")
    public ResponseEntity<Void> goToWork(@RequestParam("storeid") Integer storeId,@RequestBody EmployeeCommuteRequest commuteRequest){

        //
        storeEmployeeService.goToWork(storeId,commuteRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/leave-work")
    public ResponseEntity<Void> leaveWork(@RequestParam("storeid") Integer storeId,@RequestBody EmployeeCommuteRequest commuteRequest){
        storeEmployeeService.leaveWork(storeId,commuteRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/getCoordinates")
    public ResponseEntity<String> getCoordinates(@RequestBody String address) {
        String gps=mapService.getCoordinates(address);
        return ResponseEntity.ok(gps);
    }
}

