package com.example.Attendance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmployeeCommuteRequest {
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
}
