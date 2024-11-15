package com.example.User.dto.president;

import com.example.User.model.President;
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

    public static PresidentInfoResponse of(President president) {
        return new PresidentInfoResponse(
                president.getName(), president.getEmail(), president.getBirthDate(), president.getPhoneNumber()) ;
    }
}
