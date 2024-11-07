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
@RequestMapping("/user/store")
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
    private ResponseEntity<Void> updateStore(@RequestParam Integer storeId, @RequestBody StoreRequest storeRequest) {
        storeService.updateStore(storeId, storeRequest);
        return ResponseEntity.ok().build();
    }
}
