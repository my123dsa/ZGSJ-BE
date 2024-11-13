package com.example.Attendance.controller;

import com.example.Attendance.dto.CommuteByPresidentRequest;
import com.example.Attendance.dto.CommuteMonthlyResponse;
import com.example.Attendance.service.CommuteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/commute")
public class CommuteController {

    private final CommuteService commuteService;

    @GetMapping("/monthly")
    public ResponseEntity<List<CommuteMonthlyResponse>> getMonthlyCommuteList(
            @RequestParam int storeid,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(commuteService.getMonthlyCommuteList(storeid, year, month));
    }

    @PostMapping
    public ResponseEntity<String> addDailyCommuteByPresident(
            @RequestParam int seid, @RequestBody CommuteByPresidentRequest request)
    {
        commuteService.addDailyCommuteByPresident(request, seid);
        return ResponseEntity.ok("직원 출 퇴근 사장님이 추가 성공");
    }

    @PutMapping
    public ResponseEntity<String> updateDailyCommuteByPresident(
            @RequestParam int commuteid, @RequestBody CommuteByPresidentRequest request)
    {
        commuteService.updateDailyCommuteByPresident(request, commuteid);
        return ResponseEntity.ok("직원 출 퇴근 사장님이 수정 성공");
    }
    @DeleteMapping
    public ResponseEntity<String> deleteDailyCommuteByPresident(
            @RequestParam int commuteid)
    {
        commuteService.deleteDailyCommuteByPresident(commuteid);
        return ResponseEntity.ok("출 퇴근 기록 삭제 성공");
    }
}
