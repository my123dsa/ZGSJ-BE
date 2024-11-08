package com.example.User.controller;

import com.example.User.dto.store.StoreRequest;
import com.example.User.dto.store.StoreResponse;
import com.example.User.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    private ResponseEntity<Void> store(@RequestBody StoreRequest storeRequest) {
        storeService.registerStore(storeRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/storelist")
    private ResponseEntity<List<StoreResponse>> showStores() {
        List<StoreResponse> storeResponse = storeService.showStores();
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
