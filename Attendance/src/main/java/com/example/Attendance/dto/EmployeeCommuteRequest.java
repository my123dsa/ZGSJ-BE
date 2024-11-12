package com.example.Attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmployeeCommuteRequest {
    @NotBlank
    private String email;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
}
