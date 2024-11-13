package com.example.User.dto.presidentupdate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PresidentUpdateRequest {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private LocalDate birthDate;

}
