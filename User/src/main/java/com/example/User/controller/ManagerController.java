package com.example.User.controller;

import com.example.User.dto.manager.ManagerResponse;
import com.example.User.resolver.MasterId;
import com.example.User.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;


    @GetMapping("/president")
    public ResponseEntity<List<ManagerResponse>> getAllPresidents() {
        List<ManagerResponse> presidents = managerService.getAllPresidents();
        return ResponseEntity.ok(presidents);
    }


    // President 삭제
    @DeleteMapping("/president/{id}")
    public ResponseEntity<Void> deletePresident(@PathVariable Integer id) {
        managerService.deletePresidentById(id);
        return ResponseEntity.noContent().build();
    }

    // manager 확인
    @GetMapping("/check")
    public ResponseEntity<String> checkManager(@MasterId Integer presidentId) {
        String email = managerService.getEmailById(presidentId);
        managerService.checkmanager(email);

        return ResponseEntity.ok("관리자 확인");
    }
}
