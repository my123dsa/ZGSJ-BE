package com.example.User.controller;

import com.example.User.dto.store.StoreRequest;
import com.example.User.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
