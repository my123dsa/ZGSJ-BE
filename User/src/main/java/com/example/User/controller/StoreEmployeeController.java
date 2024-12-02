package com.example.User.controller;

import com.example.User.dto.storeemployee.EmployeeInfoResponse;
import com.example.User.dto.storeemployee.StoreEmployeeRequest;
import com.example.User.dto.storeemployee.StoreEmployeeUpdateRequest;
import com.example.User.service.EmailService;
import com.example.User.service.StoreEmployeeService;
import com.example.User.util.CryptoUtil;
import com.example.User.util.JWTUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@Slf4j
public class StoreEmployeeController {

    private final StoreEmployeeService storeemployeeService;
    private final EmailService emailService;
    private final CryptoUtil cryptoUtil;

    @PostMapping
    public ResponseEntity<String> registerEmployee(
            @Valid @RequestBody StoreEmployeeRequest request,
            @RequestParam("storeid") Integer storeid
    ) {
        storeemployeeService.register(request, storeid);
        String encryptedEmail= cryptoUtil.encryptByEmail(request.getEmail());
        String url= emailService.sendURLToEmail(request.getEmail(), storeid,encryptedEmail);
        return ResponseEntity.ok(url);
    }

    @PutMapping
    public ResponseEntity<String> updateEmployee(
            @RequestParam("seid") Integer seid,
            @Valid @RequestBody StoreEmployeeUpdateRequest request
    ) {
        storeemployeeService.updateEmployee(seid, request);
        return ResponseEntity.ok("직원 수정 성공");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteEmployee(
            @RequestParam("seid") Integer seid
    ) {
        storeemployeeService.deleteEmployee(seid);
        return ResponseEntity.ok("직원 삭제 성공");
    }

    @GetMapping("/details")
    public ResponseEntity<List<EmployeeInfoResponse>> getEmployeeInfoByStore(@RequestParam("storeid") Integer storeid)
    {
        return ResponseEntity.ok(storeemployeeService.getEmployeeInfoByStore(storeid));
    }

    @GetMapping("/resend/url")
    public ResponseEntity<String> resendUrl(@RequestParam("seid") Integer seId,@RequestParam("storeid") Integer storeId){

        String email= storeemployeeService.getEmail(seId);
        String encryptedEmail= cryptoUtil.encryptByEmail(email);
        String url= emailService.sendURLToEmail(email, storeId,encryptedEmail);

        return ResponseEntity.ok(url);
}
}

