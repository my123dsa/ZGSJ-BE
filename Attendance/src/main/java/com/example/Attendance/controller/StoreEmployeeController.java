package com.example.Attendance.controller;


import com.example.Attendance.dto.EmployeeCommuteRequest;
import com.example.Attendance.model.StoreEmployee;
import com.example.Attendance.service.CommuteService;
import com.example.Attendance.service.StoreEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StoreEmployeeController {

    private final StoreEmployeeService storeEmployeeService;
    private final CommuteService commuteService;

    @PostMapping("/{storeid}/go-to-work")
    public ResponseEntity<String> goToWork(@PathVariable("storeid") Integer storeId, @RequestBody EmployeeCommuteRequest commuteRequest){
        StoreEmployee storeEmployee=storeEmployeeService.findStoreEmployeeByEmailAndStoreId(storeId,commuteRequest);
        boolean result= commuteService.goToWork(storeEmployee);
        if(!result){
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("이전 퇴근을 찍지 않았습니다 사장님께 연락해주세요");
        }
        return ResponseEntity.ok("출근이 찍혔습니다");
    }

    @PostMapping("/{storeid}/leave-work")
    public ResponseEntity<Void> leaveWork(@PathVariable("storeid") Integer storeId,@RequestBody EmployeeCommuteRequest commuteRequest){
        StoreEmployee storeEmployee=storeEmployeeService.findStoreEmployeeByEmailAndStoreId(storeId,commuteRequest);
        commuteService.leaveWork(storeEmployee);
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/getCoordinates")
//    public ResponseEntity<String> getCoordinates(@RequestBody String address) {
//        String gps=mapService.getCoordinates(address);
//        return ResponseEntity.ok(gps);
//    }
}
