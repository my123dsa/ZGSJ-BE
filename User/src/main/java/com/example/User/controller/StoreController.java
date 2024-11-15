package com.example.User.controller;

import com.example.User.dto.store.StoreRequest;
import com.example.User.dto.store.StoreResponse;
import com.example.User.model.President;
import com.example.User.resolver.MasterId;
import com.example.User.service.EmailService;
import com.example.User.service.PresidentService;
import com.example.User.service.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final PresidentService presidentService;
    private final EmailService emailService;
    @PostMapping
    private ResponseEntity<byte[]> store(@MasterId Integer id, @RequestBody StoreRequest storeRequest) {
        List<Object> idAndEmail= storeService.registerStore(id,storeRequest);
//      (int)idAndEmail.get(0),(String)idAndEmail.get(1)
        byte[] image=emailService.sendQRToEmail("j0303p@gmail.com",4);
//        return ResponseEntity.ok();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }
     @GetMapping("/resend-QR")
     private ResponseEntity<byte[]> resendQRCode(@MasterId Integer id,@RequestParam("storeid") Integer storeId) {
         log.info("id: {} ",id);
         President president= presidentService.findById(id);
         //president.getEmail(),storeId
         byte[] image=emailService.sendQRToEmail(president.getEmail(),storeId);

         return ResponseEntity.ok()
                 .contentType(MediaType.IMAGE_PNG)
                 .body(image);
     }
    @GetMapping("/storelist")
    private ResponseEntity<List<StoreResponse>> showStores(@MasterId Integer presidentId) {
        List<StoreResponse> storeResponse = storeService.showStores(presidentId);
        return ResponseEntity.ok(storeResponse);
    }

    @PutMapping
    private ResponseEntity<Void> updateStore(@RequestParam("storeid") Integer storeId, @RequestBody StoreRequest storeRequest) {
        storeService.updateStore(storeId, storeRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    private ResponseEntity<Void> deleteStore(@RequestParam("storeid") Integer storeId) {
        storeService.deleteStore(storeId);
        //id 확인 후 삭제
        return ResponseEntity.ok().build();
    }
}
