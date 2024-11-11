package com.example.Attendance.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmployeeCommuteRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String location;
}
