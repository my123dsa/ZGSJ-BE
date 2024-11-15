package com.example.User.dto.president;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class PresidentInfoResponse {

    private String name;

    private String email;

    private LocalDate birthDate;

    private String phoneNumber;

    public static PresidentInfoResponse of(String name, String email, LocalDate birthDate, String phoneNumber) {
        return new PresidentInfoResponse(name, email, birthDate, phoneNumber);
    }
}
