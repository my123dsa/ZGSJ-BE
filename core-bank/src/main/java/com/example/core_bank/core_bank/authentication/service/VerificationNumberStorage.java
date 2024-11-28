package com.example.core_bank.core_bank.authentication.service;

import com.example.core_bank.core_bank.authentication.dto.AuthServerEmailPinNumberRequest;
import com.example.core_bank.core_bank.authentication.dto.NumberEntry;
import com.example.core_bank.core_bank.authentication.dto.ReqAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class VerificationNumberStorage {
    private final Map<String, NumberEntry> storage = new ConcurrentHashMap<>();

    public void saveNumber(String email, String number) {
        storage.put(email, NumberEntry.of(number, LocalDateTime.now().plusMinutes(10)));
    }

    public boolean verifyNumber(AuthServerEmailPinNumberRequest req) {
        NumberEntry entry = storage.remove(req.getEmail());
        if (entry != null && entry.isValid()) {
            return entry.getNumber().equals(req.getEmailPinNumber());
        }
        return false;
    }
}